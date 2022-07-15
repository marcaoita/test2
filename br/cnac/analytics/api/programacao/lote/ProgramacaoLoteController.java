package br.cnac.analytics.api.programacao.lote;

import br.cnac.analytics.api.atividade.AtividadeController;
import br.cnac.analytics.domain.dao.colaborador.ColaboradorDAO;
import br.cnac.analytics.domain.dao.programacao.ProgramacaoDAO;
import br.cnac.analytics.domain.dto.filter.FilterDTO;
import br.cnac.analytics.service.enumeration.TipoServico;
import br.cnac.analytics.service.model.colaborador.Colaborador;
import br.cnac.analytics.service.model.programacao.Programacao;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping("programacao-lote/")
@PreAuthorize("hasAnyAuthority('APPROLE_gestaoPessoas', 'APPROLE_qualidade')")
public class ProgramacaoLoteController {

    private final ProgramacaoDAO programacaoDAO;

    private final ColaboradorDAO colaboradorDAO;

    private static final String ANO_BASE = "anoBase";

    public ProgramacaoLoteController(ProgramacaoDAO programacaoDAO, ColaboradorDAO colaboradorDAO) {
        this.programacaoDAO = programacaoDAO;
        this.colaboradorDAO = colaboradorDAO;
    }

    /**
     * 
     * @param response arquivo CSV de modelo.
     * @throws IOException É lançado caso não seja possível gerar o arquivo de saída.
     */
    @GetMapping("/modelo-CSV")
    public void modeloCSV(HttpServletResponse response) throws IOException {

        String[] header = new String[] { "cpfColaborador", "cpfGestor", "dtInicio", "dtFim",
                "tipoServico", ANO_BASE, "escritorioOrig"};

        List<String[]> exemplos = new ArrayList<>();

        exemplos.add(new String[] { "542678724631", "32164321643", "04/05/2021", "09/05/2021",
        "TREINAMENTO", "2022", "BH" });

        exemplos.add(new String[] { "543234765987", "18764345621", "06/05/2021", "17/05/2021",
        "FERIAS", "2022", "BSB" });

        exemplos.add(new String[] { "12345678910", "10987654321", "07/05/2021", "18/05/2021",
        "LICENCA", "2022", "SP" });

        response.setHeader("Content-Disposition", "attachment; filename=ModeloProgramacao.csv");
        response.setContentType("text/csv");
        response.getOutputStream().write(programacaoDAO.modeloCSV(header, exemplos).toByteArray());
        response.flushBuffer();

    }

    /**
     * 
     * @param anoBase - Ano Base
     * @return Programações cujo tipo de serviço é Férias, licenças ou treinamento. 
     */
    @PostMapping("/list")
    public ResponseEntity<List<Programacao>> list(@RequestParam(value = ANO_BASE, required = false) String anoBase) {

        FilterDTO filterDTO = new FilterDTO();

        filterDTO.setAnoBase(anoBase);
        filterDTO.setTipoServico(new String[] {TipoServico.FERIAS.toString(),
                TipoServico.TREINAMENTO.toString(), TipoServico.LICENCA.toString()});

        List<Colaborador> colaboradores = colaboradorDAO.findAllByFilters(filterDTO);

        Set<Programacao> p = new HashSet<>();

        for (Colaborador c : colaboradores) {
            p.addAll(c.getProgramacoes());
        }

        return ResponseEntity.ok(p.stream().sorted().toList());
    }

    @PostMapping("/modal")
    String modal(Model model) {
        return "programacao/ferias/modal";
    }

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void upload(@RequestParam("files") MultipartFile[] files) throws IOException {

        for (MultipartFile file : files) {

            try {

                programacaoDAO.uploadLote(file.getInputStream());

            } catch (IOException | PersistenceException e) {
                throw new IOException(e);
            } catch (CsvValidationException e) {
                e.printStackTrace();
            }
        }
    }

    @GetMapping("/ferias-treinamento")
    public String index(Model model) {
        model.addAttribute(ANO_BASE, AtividadeController.getYearsAvailable());
        return "programacao/ferias/cadastro-ferias-treinamento";
    }

    /**
     * Deleta a programação com o ID passado como parâmetro.
     * @param id - Id da programação em questão.
     *
     */
    @PostMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam() String id) {

        programacaoDAO.delete(Long.valueOf(id));

    }

}
