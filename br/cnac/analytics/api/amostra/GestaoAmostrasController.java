package br.cnac.analytics.api.amostra;

import br.cnac.analytics.api.email.EmailController;
import br.cnac.analytics.api.programacao.horizontal.ProgramacaoHorizontalController;
import br.cnac.analytics.domain.dao.colaborador.ColaboradorDAO;
import br.cnac.analytics.domain.dao.programacao.ProgramacaoDAO;
import br.cnac.analytics.domain.dto.amostra.ControleAmostraDTO;
import br.cnac.analytics.domain.dto.filter.FilterDTO;
import br.cnac.analytics.service.model.colaborador.Colaborador;
import br.cnac.analytics.service.model.programacao.Programacao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping("controle-geracao-amostra/")
@PreAuthorize("hasAnyAuthority('APPROLE_gestores', 'APPROLE_qualidade', 'APPROLE_csa')")
public class GestaoAmostrasController {

    private final ColaboradorDAO colaboradorDAO;

    private final ProgramacaoDAO programacaoDAO;

    public GestaoAmostrasController(ColaboradorDAO colaboradorDAO, ProgramacaoDAO programacaoDAO) {
        this.colaboradorDAO = colaboradorDAO;
        this.programacaoDAO = programacaoDAO;
    }

    /**
     * @param filterDTO - Objeto do tipo <code>FilterDTO</code> com os filtros solicitados pelo usuário.
     * @return Retorna as programações dado os filtros passados como parâmetro.
     */
    public static Set<Programacao> findByFilters(@Valid FilterDTO filterDTO,
                                                 ColaboradorDAO colaboradorDAO) {
        List<Colaborador> colaboradores = colaboradorDAO.findAllByFilters(filterDTO);

        Set<Programacao> p = new HashSet<>();

        for (Colaborador c : colaboradores) {
                p.addAll(c.getProgramacoes());
        }

        return p;
    }

    @GetMapping("/index")
    public String listProgramacaoTabelada(Model model) {

        ProgramacaoHorizontalController.setModelColaboradores(model);

        return "amostra/controle-geracao-amostra";
    }

    /**
     * @param cnpj  - CNPJ do cliente em questão.
     * @param model - dados para transmissão ao front-end.
     * @return retorna o página com a todas as programações por cliente.
     */
    @PostMapping("/get-modal")
    public String getOneClient(
            @RequestParam(value = "cnpj") String cnpj,
            @RequestParam(value = "siglaCoop") String siglaCoop,
            @RequestParam(value = "id") String id,
            @RequestParam(value = "escopos") String escopos,
            Model model) {

        model.addAttribute("controleAmostraDTO", new ControleAmostraDTO());
        model.addAttribute("cnpj", cnpj);
        model.addAttribute("siglaCoop", siglaCoop);
        model.addAttribute("id", id);
        model.addAttribute("escopos", escopos);

        return "amostra/modal-controle-amostra";
    }

    /**
     * Persiste as informações de dtBase e se amostra foi gerada no Banco
     *
     * @param controleAmostraDTO Objeto do tipo <code>ControleAmostraDTO</code>
     * @param result             - Validação dos dados.
     * @throws BindException - É lançado quando há erros nos dados.
     * @throws IOException   - É lançada quando não é possível enviar e-mail.
     */
    @PostMapping("/update")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void add(@Valid ControleAmostraDTO controleAmostraDTO, BindingResult result) throws BindException, IOException {

        if (result.hasErrors()) {
            throw new BindException(result);
        }

        Programacao p = programacaoDAO.findOne(Long.valueOf(controleAmostraDTO.getId()));

        p.setDtBase(controleAmostraDTO.getDtBase());
        p.setAmostraGerada(controleAmostraDTO.isAmostraGerada());

        //Quando a amostra é disponibilizada, é enviado um e-mail notificando os atores da programação em questão.
        EmailController.sendEmailDispAmostra(p);

        programacaoDAO.save(p);
    }

    @PostMapping("/list")
    public ResponseEntity<List<Programacao>> list(@Valid FilterDTO filterDTO) {

        Set<Programacao> p = findByFilters(filterDTO, colaboradorDAO);

        if (!"".equals(filterDTO.getAmostra()))
            p.removeIf(prog -> prog.isAmostraGerada() != Boolean.parseBoolean(filterDTO.getAmostra()));

        if (filterDTO.getMes() != null)
            p.removeIf(prog -> !Arrays.asList(filterDTO.getMes()).contains(prog.getDtPrevAmostra().toLocalDate().getMonthValue()));

        return ResponseEntity.ok(p.stream().sorted(Comparator.comparing(Programacao::getDtPrevAmostra)).toList());
    }
}
