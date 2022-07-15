package br.cnac.analytics.api.colaborador;

import br.cnac.analytics.domain.dao.cargo.CargoDAO;
import br.cnac.analytics.domain.dao.colaborador.ColaboradorDAO;
import br.cnac.analytics.domain.dto.colaborador.ColaboradorDTO;
import br.cnac.analytics.domain.dto.filter.FilterDTO;
import br.cnac.analytics.service.interfaces.colaborador.ColaboradorSimple;
import br.cnac.analytics.service.model.colaborador.Colaborador;
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

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.*;


@SuppressWarnings("SameReturnValue")
@Controller
@RequestMapping("colaborador/")
@PreAuthorize("hasAnyAuthority('APPROLE_gestores','APPROLE_gestaoPessoas', 'APPROLE_qualidade')")
public class ColaboradorController {

    private static final String[] AREA = {"Auditoria", "Supervisão", "Qualidade", "Gerência"};
    private static final String[] OFFICES = {"BH", "BSB", "SP", "CSA", "TI", "UAD"};
    private static final String[] TIPO_GESTORES = {"diretores", "gerentes", "supervisores"};
    private static ColaboradorDAO componentColaDAO;
    private final ColaboradorDAO colaboradorDAO;
    private final CargoDAO cargoDAO;

    public ColaboradorController(ColaboradorDAO colaboradorDAO, CargoDAO cargoDAO) {
        this.colaboradorDAO = colaboradorDAO;
        this.cargoDAO = cargoDAO;
    }

    /**
     * @return Retorna um JSON com os colaboradores classificados por escritório e
     * cargos de gerência.
     */
    public static Map<String, List<ColaboradorSimple>> getJsonColaboradores() {

        Set<ColaboradorSimple> colaboradores = componentColaDAO.findAllSimple();

        HashMap<String, List<ColaboradorSimple>> map = new HashMap<>();

        // Tipo de áreas programáveis.
        List<String> areas = Arrays.asList(AREA);

        map.put(OFFICES[0], colaboradores.stream()
                .filter(c -> (areas.contains(c.getArea()))
                        && c.getEscritorioOrigem().equals(OFFICES[0]))
                .sorted(Comparator.comparing(ColaboradorSimple::getNome))
                .toList());

        map.put(OFFICES[1], colaboradores.stream()
                .filter(c -> (areas.contains(c.getArea()))
                        && c.getEscritorioOrigem().equals(OFFICES[1]))
                .sorted(Comparator.comparing(ColaboradorSimple::getNome))
                .toList());

        map.put(OFFICES[2], colaboradores.stream()
                .filter(c -> (areas.contains(c.getArea()))
                        && c.getEscritorioOrigem().equals(OFFICES[2]))
                .sorted(Comparator.comparing(ColaboradorSimple::getNome))
                .toList());

        map.put(OFFICES[3], colaboradores.stream()
                .filter(c -> (areas.contains(c.getArea()))
                        && c.getEscritorioOrigem().equals(OFFICES[3]))
                .sorted(Comparator.comparing(ColaboradorSimple::getNome))
                .toList());

        map.put(OFFICES[4], colaboradores.stream()
                .filter(c -> (areas.contains(c.getArea()))
                        && c.getEscritorioOrigem().equals(OFFICES[4]))
                .sorted(Comparator.comparing(ColaboradorSimple::getNome))
                .toList());

        map.put(TIPO_GESTORES[0], colaboradores
                .stream()
                .filter(c -> c.getArea().equals("Diretoria Executiva"))
                .sorted(Comparator.comparing(ColaboradorSimple::getNome))
                .toList());

        map.put(TIPO_GESTORES[1], colaboradores
                .stream()
                .filter(c -> c.getArea().equals("Gerência"))
                .sorted(Comparator.comparing(ColaboradorSimple::getNome))
                .toList());

        map.put(TIPO_GESTORES[2], colaboradores
                .stream()
                .filter(c -> c.getArea().equals("Supervisão"))
                .sorted(Comparator.comparing(ColaboradorSimple::getNome))
                .toList());

        return map;
    }

    @GetMapping("/index")
    @PreAuthorize("hasAnyAuthority('APPROLE_gestaoPessoas', 'APPROLE_qualidade')")
    public String index(Model model) {
        model.addAttribute("colaborador", new ColaboradorDTO());
        model.addAttribute("cargos", cargoDAO.findAll());
        return "colaborador/cadastro-colaboradores";
    }

    @PostMapping("/list")
    public ResponseEntity<List<Colaborador>> list() {
        return ResponseEntity.ok(colaboradorDAO.findAll());
    }

    @PostMapping("/modal")
    public String modal(Model model, @RequestParam(value = "cpf") String cpf,
                        @RequestParam(value = "anoBase") String anoBase) {

        Colaborador c = colaboradorDAO.findOne(cpf, anoBase);
        model.addAttribute("colaborador", c);
        model.addAttribute("tipo", "colaborador");
        model.addAttribute("programacoes", c.getProgramacoes().stream().sorted().toList());
        return "programacao/modal/list-programacoes";
    }

    @PostMapping("/listAllData")
    public ResponseEntity<List<Colaborador>> listAllData(@Valid FilterDTO filterDTO) {


        // Tipo de áreas programáveis.
        List<String> areas = Arrays.asList(AREA);

        return ResponseEntity.ok(colaboradorDAO
                .findAllByFilters(filterDTO)
                .stream()
                .filter(c -> (areas.contains(c.getCargo().getArea()) && c.getStatusAtual().equals("Ativo")))
                .sorted()
                .toList());
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('APPROLE_gestaoPessoas', 'APPROLE_qualidade')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void add(@Valid ColaboradorDTO colaboradorDTO, BindingResult result) throws BindException {

        if (result.hasErrors()) {
            throw new BindException(result);
        }

        colaboradorDAO.save(colaboradorDTO.convertDTOToEntity());
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAnyAuthority('APPROLE_gestaoPessoas', 'APPROLE_qualidade')")
    @ResponseStatus(HttpStatus.NO_CONTENT) //
    public void delete(@RequestParam("cpf") String cpf) throws ClassNotFoundException {
        try {
            colaboradorDAO.delete(cpf);
        } catch (EmptyResultDataAccessException ex) {
            throw new ClassNotFoundException("Entidade não encontrada no servidor de dados!");
        } catch (ConstraintViolationException e) {
            throw new ConstraintViolationException("Colaborador já alocado em uma programação",
                    e.getSQLException(),
                    e.getMessage());
        }
    }

    @PostConstruct
    private void initAtividade() {
        componentColaDAO = this.colaboradorDAO;
    }

}
