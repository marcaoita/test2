package br.cnac.analytics.api.monitoramento;

import br.cnac.analytics.api.atividade.AtividadeController;
import br.cnac.analytics.api.colaborador.ColaboradorController;
import br.cnac.analytics.domain.dao.monitoramento.MonitoramentoDAO;
import br.cnac.analytics.domain.dto.filter.FilterDTO;
import br.cnac.analytics.domain.dto.monitoramento.MonitoramentoDTO;
import br.cnac.analytics.service.interfaces.colaborador.ColaboradorSimple;
import br.cnac.analytics.service.interfaces.monitoramento.MonitoramentoRel;
import br.cnac.analytics.service.model.monitoramento.Monitoramento;
import com.opencsv.exceptions.CsvValidationException;
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

import javax.persistence.PersistenceException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping("monitoramento/")
@PreAuthorize("hasAnyAuthority('APPROLE_gestores', 'APPROLE_qualidade', 'APPROLE_csa')")
public class MonitoramentoController {

    private final MonitoramentoDAO monitoramentoDAO;

    public MonitoramentoController(MonitoramentoDAO monitoramentoDAO) {
        this.monitoramentoDAO = monitoramentoDAO;
    }

    @GetMapping("/monitoramento-atividade")
    public String getMonitoramentoAtividade(Model model) {
        model.addAttribute("anos", AtividadeController.getYearsAvailable());
        return "monitoramento/geral/atividades";
    }

    @GetMapping("/catalogacao")
    public String getCatalogacao(Model model) {
        model.addAttribute("anos", AtividadeController.getYearsAvailable());
        return "monitoramento/geral/catalogacao";
    }

    @GetMapping("/graficos")
    public String getGraficos(Model model) {
        model.addAttribute("anos", AtividadeController.getYearsAvailable());
        return "monitoramento/graficos/grafico";
    }

    @GetMapping("/av-critica")
    public String getAvCritica(Model model) {
        model.addAttribute("anos", AtividadeController.getYearsAvailable());
        Map<String, List<ColaboradorSimple>> map = ColaboradorController.getJsonColaboradores();
        model.addAttribute("gerentes", map.get("gerentes"));
        return "monitoramento/ac/auditoria-cooperativa";
    }


    @GetMapping("/importacoes")
    public String getImportacoes(Model model) {
        return "importacoes/importacoes";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('APPROLE_gestores', 'APPROLE_qualidade')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void add(@Valid MonitoramentoDTO monitoramentoDTO, BindingResult result) throws BindException {

        if (result.hasErrors()) {
            throw new BindException(result);
        }

        monitoramentoDAO.save(monitoramentoDTO.convertDTOToEntity());
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('APPROLE_gestores', 'APPROLE_qualidade')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) throws ClassNotFoundException {
        try {
            monitoramentoDAO.delete(Long.parseLong(id));
        } catch (EmptyResultDataAccessException ex) {
            throw new ClassNotFoundException("Entidade não encontrada no servidor de dados!");
        }
    }

    @PostMapping("/list")
    public ResponseEntity<List<Monitoramento>> list(Model model) {
        return ResponseEntity.ok(monitoramentoDAO.findAll());
    }

    @PostMapping("/relatorio")
    public ResponseEntity<List<MonitoramentoRel>> list(@Valid FilterDTO filterDTO) {
        return ResponseEntity.ok(monitoramentoDAO.getRelatorio(filterDTO));
    }

    /**
     * @param files - Arquivos do tipo CSV para importação em lote.
     * @throws IOException - É lançado caso haja problemas com os arquivos.
     */
    @PostMapping("/upload")
    @PreAuthorize("hasAnyAuthority('APPROLE_gestores', 'APPROLE_qualidade')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void upload(@RequestParam("files") MultipartFile[] files) throws IOException {

        for (MultipartFile file : files) {

            try {

                monitoramentoDAO.uploadLote(file.getInputStream());

            } catch (IOException | PersistenceException e) {
                throw new IOException(e);
            } catch (CsvValidationException e) {
                e.printStackTrace();
            }
        }
    }


}
