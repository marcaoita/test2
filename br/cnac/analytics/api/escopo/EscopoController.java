package br.cnac.analytics.api.escopo;

import br.cnac.analytics.domain.dao.escopo.EscopoDAO;
import br.cnac.analytics.domain.dto.escopo.EscopoDTO;
import br.cnac.analytics.service.model.escopo.Escopo;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping("escopo/")
@PreAuthorize("hasAuthority('APPROLE_qualidade')")
public class EscopoController {

    final
    EscopoDAO escopoDAO;

    public EscopoController(EscopoDAO escopoDAO) {
        this.escopoDAO = escopoDAO;
    }

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("escopo", new Escopo());
        return "escopo/cadastro-escopo";
    }

    @PostMapping("/list")
    public ResponseEntity<List<Escopo>> list() {
        return ResponseEntity.ok(escopoDAO.findAll());
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void add(@Valid EscopoDTO escopoDTO, BindingResult result) throws BindException {

        if (result.hasErrors()) {
            throw new BindException(result);
        }

        escopoDAO.save(escopoDTO.convertDTOToEntity());
    }

    @PostMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT) //
    public void delete(@RequestParam("id") String id) throws ClassNotFoundException {
        try {
            escopoDAO.delete(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new ClassNotFoundException("Entidade não encontrada no servidor de dados!");
        }
    }
}
