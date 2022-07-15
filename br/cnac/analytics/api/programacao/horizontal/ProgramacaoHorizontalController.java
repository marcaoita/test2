package br.cnac.analytics.api.programacao.horizontal;

import br.cnac.analytics.api.amostra.GestaoAmostrasController;
import br.cnac.analytics.api.atividade.AtividadeController;
import br.cnac.analytics.api.colaborador.ColaboradorController;
import br.cnac.analytics.domain.dao.colaborador.ColaboradorDAO;
import br.cnac.analytics.domain.dto.filter.FilterDTO;
import br.cnac.analytics.service.interfaces.colaborador.ColaboradorSimple;
import br.cnac.analytics.service.model.programacao.Programacao;
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
import java.util.Set;


@SuppressWarnings("SameReturnValue")
@Controller

@RequestMapping("programacao-horizontal/")
@PreAuthorize("hasAnyAuthority('APPROLE_gestores', 'APPROLE_gestaoPessoas', 'APPROLE_qualidade', 'APPROLE_csa')")
public class ProgramacaoHorizontalController {

    private final ColaboradorDAO colaboradorDAO;

    public ProgramacaoHorizontalController(ColaboradorDAO colaboradorDAO) {
        this.colaboradorDAO = colaboradorDAO;
    }

    public static void setModelColaboradores(Model model) {
        Map<String, List<ColaboradorSimple>> map = ColaboradorController.getJsonColaboradores();
        model.addAttribute("anoBase", AtividadeController.getYearsAvailable());
        model.addAttribute("diretores", map.get("diretores"));
        model.addAttribute("gerentes", map.get("gerentes"));
        model.addAttribute("supervisores", map.get("supervisores"));
    }

    @GetMapping("/index")
    public String listProgramacaoTabelada(Model model) {

        setModelColaboradores(model);

        return "programacao/horizontal/programacao-horizontal";
    }

    @PostMapping("/list")
    public ResponseEntity<List<Programacao>> list(@Valid FilterDTO filterDTO) {

        Set<Programacao> p = GestaoAmostrasController.findByFilters(filterDTO, colaboradorDAO);

        if (!"".equals(filterDTO.getAmostra()))
            p.removeIf(prog -> prog.isAmostraGerada() != Boolean.parseBoolean(filterDTO.getAmostra()));

        if (filterDTO.getMes() != null)
            p.removeIf(prog -> !Arrays.asList(filterDTO.getMes()).contains(prog.getDtInicio().toLocalDate().getMonthValue()));

        return ResponseEntity.ok(p.stream().sorted(Programacao::compareTo).toList());
    }

}
