package br.cnac.analytics.api.cliente;

import br.cnac.analytics.api.atividade.AtividadeController;
import br.cnac.analytics.domain.dao.cliente.ClienteDAO;
import br.cnac.analytics.domain.dto.cliente.ClienteDTO;
import br.cnac.analytics.service.interfaces.cliente.ClientSimple;
import br.cnac.analytics.service.model.cliente.Cliente;
import com.opencsv.exceptions.CsvValidationException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping("cliente/")
@PreAuthorize("hasAnyAuthority('APPROLE_gestores', 'APPROLE_qualidade')")
public class ClienteController {

    private static final String ANO_BASE = "anoBase";
    private static ClienteDAO componentClienteDao;
    private static TreeMap<String, ClientSimple> clientesByNumCoop;
    final
    ClienteDAO clienteDAO;

    public ClienteController(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }


    /**
     * Método responsável por carregar na memória dados de cliente e criar uma árvore binária de busca, cujo a chave é o número da cooperativa.
     */
    public static void loadClientsMap() {

        clientesByNumCoop = new TreeMap<>(componentClienteDao.findSimple()
                .stream()
                .collect(
                        Collectors
                                .toMap(ClientSimple::getNumCoop, c -> c)));
    }

    public static ClientSimple getClienteByNumCoop(String numCoop) {

        ClientSimple c = clientesByNumCoop.get(numCoop);

        if (c == null)
            throw new IllegalArgumentException("Cooperativa com o número: " + numCoop + " não encontrada na base de clientes.");

        return clientesByNumCoop.get(numCoop);
    }

    /**
     * @param cnpj    do cliente em questão.
     * @param anoBase que será considerada para filtrar as programações do cliente.
     * @return Retorna um objeto do tipo <code>Cliente</code>
     */
    public static Cliente getClient(String cnpj, String anoBase) {
        return componentClienteDao.findOne(cnpj, anoBase);
    }

    @GetMapping("/index")
    @PreAuthorize("hasAuthority('APPROLE_qualidade')")
    public String index(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "cliente/cadastro-cliente";
    }

    @PostMapping("/list")
    public ResponseEntity<List<Cliente>> list() {
        return ResponseEntity.ok(clienteDAO.findAll());
    }

    @PostMapping("/listAll")
    public ResponseEntity<Set<Cliente>> listAll(
            @RequestParam(value = "anoBase[]", required = false) String[] anoBase,
            @RequestParam(value = "offices[]", required = false) String[] offices,
            @RequestParam(value = "tipoServico[]", required = false) String[] tipoServico,
            @RequestParam(value = "gestores[]", required = false) String[] gestores) {

        return ResponseEntity.ok(clienteDAO.findAllByFilters(anoBase, offices, tipoServico, gestores));
    }


    @PostMapping("/add")
    @PreAuthorize("hasAuthority('APPROLE_qualidade')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void add(@Valid ClienteDTO clienteDTO, BindingResult result) throws BindException {
        if (result.hasErrors()) {
            throw new BindException(result);
        }

        clienteDAO.save(clienteDTO.convertDTOToEntity());
    }

    @PostConstruct
    private void initCliente() {
        componentClienteDao = this.clienteDAO;
    }

    @PostMapping("/modal")
    public String getOneClient(
            @RequestParam(value = "cnpj") String cnpj,
            @RequestParam(value = ANO_BASE, required = false) String anoBase,
            Model model) {

        if (anoBase == null)
            anoBase = AtividadeController.getBiggerYear().get(0);

        Cliente cliente = clienteDAO.findOne(cnpj, anoBase);

        model.addAttribute("numEscopos", cliente.quantEscopos());
        model.addAttribute("horasContratadas", cliente.diasContratados());
        model.addAttribute("cliente", cliente);
        model.addAttribute("tipo", "cliente");
        model.addAttribute("programacoes", cliente.getProgramacoes().stream().sorted().toList());

        return "programacao/modal/list-programacoes";
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('APPROLE_qualidade')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam("cnpj") String cnpj) throws ClassNotFoundException {
        try {
            clienteDAO.delete(cnpj);

        } catch (IllegalStateException e) {
            throw new IllegalStateException("Cliente está em uso!", e);
        } catch (EmptyResultDataAccessException e) {
            throw new ClassNotFoundException("Entidade não encontrada no servidor de dados!", e);
        } catch (ConstraintViolationException e) {
            throw new ConstraintViolationException("Cliente já alocado em uma programação", e.getSQLException(),
                    e.getMessage());
        }
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('APPROLE_qualidade')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void upload(@RequestParam("files") MultipartFile[] files) throws IOException {

        for (MultipartFile file : files) {

            try {

                clienteDAO.uploadLote(file.getInputStream());

            } catch (IOException | PersistenceException e) {
                throw new IOException(e);
            } catch (CsvValidationException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    @GetMapping("/modeloCSV")
    public void modeloCSV(HttpServletResponse response) throws IOException {

        String[] header = new String[]{"cnpjCliente", "numCoop", "siglaCoop", "razaoSocial", "codCentral",
                "siglaCentral", "codSistema", "sisbr", "segmentacao", "classe", "municipio", "estado"};

        response.setHeader("Content-Disposition", "attachment; filename=ModeloCliente.csv");
        response.setContentType("text/csv");
        response.getOutputStream().write(clienteDAO.modeloCSV(header).toByteArray());
        response.flushBuffer();

    }

}
