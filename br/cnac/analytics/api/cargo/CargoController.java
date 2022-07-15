package br.cnac.analytics.api.cargo;

import br.cnac.analytics.domain.dao.cargo.CargoDAO;
import br.cnac.analytics.domain.dto.cargo.CargoDTO;
import br.cnac.analytics.service.model.cargo.Cargo;

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
@RequestMapping("cargo/")
@PreAuthorize("hasAnyAuthority('APPROLE_gestaoPessoas', 'APPROLE_qualidade')")
public class CargoController {

    private final CargoDAO cargoDAO;

    public CargoController(CargoDAO cargoDAO) {
        this.cargoDAO = cargoDAO;
    }

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("cargo", new Cargo());
        return "cargo/cadastro-cargo";
    }

    @PostMapping("/list")
    public ResponseEntity<List<Cargo>> list() {
        return ResponseEntity.ok(cargoDAO.findAll());
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void add(@Valid CargoDTO cargoDTO, BindingResult result) throws BindException {

        if (result.hasErrors()) {
            throw new BindException(result);
        }

        cargoDAO.save(cargoDTO.convertDTOToEntity());
    }

    @PostMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam("id") String id) throws ClassNotFoundException {
        try {
            cargoDAO.delete(id);
        } catch (EmptyResultDataAccessException ex) {
            throw new ClassNotFoundException("Entidade não encontrada no servidor de dados!");
        }
    }

}
