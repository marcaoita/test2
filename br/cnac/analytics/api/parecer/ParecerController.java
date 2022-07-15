package br.cnac.analytics.api.parecer;

import javax.validation.Valid;

import br.cnac.analytics.api.atividade.AtividadeController;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.cnac.analytics.domain.dao.parecer.ParecerDAO;
import br.cnac.analytics.domain.dao.programacao.ProgramacaoDAO;
import br.cnac.analytics.domain.dto.parecer.ParecerDTO;
import br.cnac.analytics.service.model.parecer.Parecer;
import br.cnac.analytics.service.model.programacao.Programacao;

import java.util.List;

@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping("parecer/")
@PreAuthorize("hasAnyAuthority('APPROLE_gestores', 'APPROLE_qualidade')")
public class ParecerController {

    final ParecerDAO parecerDAO;
    final ProgramacaoDAO programacaoDAO;

    public ParecerController(ParecerDAO parecerDAO, ProgramacaoDAO programacaoDAO) {
        this.parecerDAO = parecerDAO;
        this.programacaoDAO = programacaoDAO;
    }

    @PostMapping("/modal")
    public String index(Model model, @RequestParam("id") String id) {

        model.addAttribute("parecerDTO", new ParecerDTO());

        Programacao p = programacaoDAO.findOne(Long.parseLong(id));

        if (p.getParecer() == null)
            p.setParecer(new Parecer());

        model.addAttribute("p", p);

        return "parecer/modal-parecer";
    }

    @GetMapping("/index")
    public String pareceres(Model model) {
        model.addAttribute("anoBase", AtividadeController.getYearsAvailable());
        return "parecer/pareceres";
    }

    @PostMapping("/list")
    public ResponseEntity<List<Parecer>> list(Model model, @RequestParam("anoBase") String anoBase) {

        if (anoBase == null)
            anoBase = AtividadeController.getBiggerYear().get(0);

        List<Parecer> pareceres = parecerDAO.findAll();

        String finalAnoBase = anoBase;

        pareceres.removeIf(p -> !p.getProgramacao().getAnoBase().equals(finalAnoBase));

        return ResponseEntity.ok(pareceres);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void add(@Valid ParecerDTO parecerDTO, BindingResult result) throws BindException {

        if (result.hasErrors()) {
            throw new BindException(result);
        }

        parecerDAO.save(parecerDTO.convertDTOToEntity());

    }

    @GetMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) throws ClassNotFoundException {
        try {
            parecerDAO.delete(Long.parseLong(id));
        } catch (EmptyResultDataAccessException ex) {
            throw new ClassNotFoundException("Entidade não encontrada no servidor de dados!");
        }
    }

}
