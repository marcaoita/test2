package br.cnac.analytics.api.programacao.vertical;

import br.cnac.analytics.api.atividade.AtividadeController;
import br.cnac.analytics.api.colaborador.ColaboradorController;
import br.cnac.analytics.domain.dao.atividade.AtividadeDAO;
import br.cnac.analytics.domain.dao.cargo.CargoDAO;
import br.cnac.analytics.domain.dao.colaborador.ColaboradorDAO;
import br.cnac.analytics.domain.dto.filter.FilterDTO;
import br.cnac.analytics.service.interfaces.colaborador.ColaboradorSimple;
import br.cnac.analytics.service.model.colaborador.Colaborador;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping("programacao-vertical/")
@PreAuthorize("hasAnyAuthority('APPROLE_gestores', 'APPROLE_csa', 'APPROLE_qualidade')")
public class ProgramacaoVerticalController {

    private final AtividadeDAO atividadeDAO;

    private final ColaboradorDAO colaboradorDAO;

    private final CargoDAO cargoDAO;

    public ProgramacaoVerticalController(AtividadeDAO atividadeDAO, ColaboradorDAO colaboradorDAO, CargoDAO cargoDAO) {
        this.atividadeDAO = atividadeDAO;
        this.colaboradorDAO = colaboradorDAO;
        this.cargoDAO = cargoDAO;
    }


    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("anoBase", atividadeDAO.findAllSimple());
        model.addAttribute("anoBasePadrao", AtividadeController.getBiggerYear().get(0));
        model.addAttribute("cargos", cargoDAO.findAll().stream().filter(c -> c.getArea().equals("Auditoria")).sorted()
                .toList());

        Map<String, List<ColaboradorSimple>> map = ColaboradorController.getJsonColaboradores();

        model.addAttribute("diretores", map.get("diretores"));
        model.addAttribute("gerentes", map.get("gerentes"));
        model.addAttribute("supervisores", map.get("supervisores"));

        return "programacao/vertical/programacao-vertical";
    }

    @PostMapping("/list")
    public ResponseEntity<List<Colaborador>> listAllData(@Valid FilterDTO filterDTO) {

        List<String> areas;

        if (filterDTO.getAreas() == null) {
            areas = List.of("Auditoria");
        } else {
            areas = Arrays.asList(filterDTO.getAreas());
        }

        List<String> finalAreas = areas;
        return ResponseEntity.ok(colaboradorDAO.findAllByFilters(filterDTO).stream()
                .filter(c -> finalAreas.contains(c.getCargo().getArea()) && (!c.getProgramacoes().isEmpty() || c.getStatusAtual().equals("Ativo")))
                .sorted()
                .toList());
    }


}
