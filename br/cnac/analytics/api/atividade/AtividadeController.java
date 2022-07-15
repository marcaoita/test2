package br.cnac.analytics.api.atividade;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import br.cnac.analytics.domain.dto.filter.FilterDTO;
import com.opencsv.exceptions.CsvValidationException;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import br.cnac.analytics.domain.dao.atividade.AtividadeDAO;
import br.cnac.analytics.domain.dao.cliente.ClienteDAO;
import br.cnac.analytics.domain.dao.escopo.EscopoDAO;
import br.cnac.analytics.domain.dto.atividade.AtividadeDTO;
import br.cnac.analytics.service.enumeration.StatusAtividade;
import br.cnac.analytics.service.model.atividade.chave_composta.PkAtividade;
import br.cnac.analytics.service.model.atividade.entity.Atividade;
import br.cnac.analytics.service.interfaces.atividade.AtividadeSimple;

@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping("atividade/")
@PreAuthorize("hasAnyAuthority('APPROLE_gestores', 'APPROLE_qualidade')")
public class AtividadeController {

    private final AtividadeDAO atividadeDAO;
    private final EscopoDAO escopoDAO;
    private final ClienteDAO clienteDAO;
    private static List<String> yearsAvailable;
    private static AtividadeDAO componentAtividadeDao;

    public AtividadeController(AtividadeDAO atividadeDAO, EscopoDAO escopoDAO, ClienteDAO clienteDAO) {
        this.atividadeDAO = atividadeDAO;
        this.escopoDAO = escopoDAO;
        this.clienteDAO = clienteDAO;
    }

    @PostConstruct
    private void initAtividade() {
        componentAtividadeDao = this.atividadeDAO;
    }

    @PreAuthorize("hasAuthority('APPROLE_qualidade')")
    @GetMapping("/index")
    public String index(Model model) {

        model.addAttribute("atividade", new AtividadeDTO());
        model.addAttribute("escopos", escopoDAO.findAll());
        model.addAttribute("clientes", clienteDAO.findAll());
        model.addAttribute("anoBase", getYearsAvailable());

        return "atividade/cadastro-atividade";
    }

    @PostMapping("/list")
    public ResponseEntity<List<Atividade>> list(@Valid FilterDTO filterDTO) {
        return ResponseEntity.ok(atividadeDAO.findAll(filterDTO.getAnoBase(), new String[] {"ATIVA", "CANCELADA", "SUSPENSA"}));
    }

    @GetMapping("/find")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<List<Atividade>> find(@RequestParam("cnpj") String cnpj,
            @RequestParam("anoBase") String anoBase, @RequestParam("tipoServico") String tipoServico) {

        // Caso o tipo de Serviço seja CSA o banco deverá buscar todos os tipos, para
        // isso basta passar null como parâmetro.
        if (tipoServico.equalsIgnoreCase("CSA"))
            tipoServico = null;

        return ResponseEntity.ok(atividadeDAO.findAll(anoBase, cnpj, tipoServico)
                .stream()
                .filter(a -> a.getStatusAtividade().equals(StatusAtividade.ATIVA)).toList());

    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('APPROLE_qualidade')")
    public void add(@Valid AtividadeDTO atividadeDTO, BindingResult result) throws BindException, ParseException {
        if (result.hasErrors()) {
            throw new BindException(result);
        }

        atividadeDAO.save(atividadeDTO.convertDTOToEntity());

        loadYearsAvailable();
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('APPROLE_qualidade')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam("cnpj") String cnpj, @RequestParam("numEscopo") String numEscopo,
            @RequestParam("anoBase") String anoBase)
            throws ConstraintViolationException {
        try {

            PkAtividade chave = new PkAtividade(cnpj, numEscopo, anoBase);
            atividadeDAO.delete(chave);

            loadYearsAvailable();

        } catch (IllegalStateException ex) {

            throw new IllegalStateException("Cliente está em uso!", ex);

        } catch (ConstraintViolationException e) {
            throw new ConstraintViolationException("Atividade já cadastrada em uma programação", e.getSQLException(),
                    e.getMessage());
        }
    }

    /**
     * 
     * Retorna um arquivo CSV modelo
     * 
     * @param response - Retorna um arquivo CSV de modelo.
     * @throws IOException É lançado quando o arquivo CSV contém erros.
     */
    @GetMapping("/modeloCSV")
    public void modeloCSV(HttpServletResponse response) throws IOException {

        String[] header = new String[] { "cnpjCliente", "numEscopo", "numContratoOrig", "tipoAuditoria", "anoBase",
                "horasVendidas", "statusAtividade" };

        response.setHeader("Content-Disposition", "attachment; filename=ModeloAtividade.csv");
        response.setContentType("text/csv");
        response.getOutputStream().write(atividadeDAO.modeloCSV(header).toByteArray());
        response.flushBuffer();

    }

/**
 * 
 * @param files - Arquivos do tipo CSV para importação em lote. 
 * @throws IOException - É lançado caso haja problemas com os arquivos. 
 */
    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void upload(@RequestParam("files") MultipartFile[] files) throws IOException {

        for (MultipartFile file : files) {

            try {

                atividadeDAO.uploadLote(file.getInputStream());
                loadYearsAvailable();

            } catch (IOException | PersistenceException e) {
                throw new IOException(e);
            } catch (CsvValidationException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 
     * @return Retorna o maior ano disponível no sistema.
     */
    public static List<String> getBiggerYear() {

        List<String> anos = new ArrayList<>();
        anos.add(yearsAvailable.get(0));

        return anos;
    }

    /**
     * 
     * @return Retorna os anos disponíveis no sistema. 
     */
    public static List<String> getYearsAvailable() {
        return yearsAvailable;
    }

    /**
     * Carrega os anos disponíveis em memória.
     */
    public static void loadYearsAvailable() {

        yearsAvailable = componentAtividadeDao.findAllSimple().stream()
                .map(AtividadeSimple::getAnoBase)
                .sorted(Collections.reverseOrder())
                .limit(3)
                .toList();

    }

}
