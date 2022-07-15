package br.cnac.analytics.api.relatorio;

import br.cnac.analytics.api.atividade.AtividadeController;
import br.cnac.analytics.domain.dao.cliente.ClienteDAO;
import br.cnac.analytics.service.interfaces.cliente.ClienteSemProgramacaoRepository;
import br.cnac.analytics.service.interfaces.relatorio.ControleCSA;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;


@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping("relatorios/")
public class RelatoriosController {

    private final ClienteDAO clienteDAO;

    public RelatoriosController(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    @GetMapping("/clientes-sem-programacao")
    public String getOneClient(
            @RequestParam(value = "anoBase", required = false) String anoBase,
            Model model) {

        Set<ClienteSemProgramacaoRepository> relatorio;

        if (anoBase == null)
            anoBase = AtividadeController.getBiggerYear().get(0);

        relatorio = clienteDAO.findCoopSemProgramacao(anoBase);

        model.addAttribute("relatorio", relatorio);
        model.addAttribute("anos", AtividadeController.getYearsAvailable());

        return "relatorio/cliente/clientes-sem-programacao";
    }

    @GetMapping("/relatorio-csa")
    public String getRelatorioCSA(Model model) {
        model.addAttribute("anos", AtividadeController.getYearsAvailable());
        return "relatorio/csa/relatorio-csa";
    }

    @PostMapping("/relatorio-csa-dados")
    public ResponseEntity<List<ControleCSA>> getControleCSA(@RequestParam("anoBase") String anoBase) {
        return ResponseEntity.ok(clienteDAO.getControleCSA(anoBase));
    }


}
