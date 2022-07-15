package br.cnac.analytics.api.configuracao;

import br.cnac.analytics.domain.dao.configuracao.ConfiguracaoDAO;
import br.cnac.analytics.domain.dto.configuracao.ConfiguracaoDTO;
import br.cnac.analytics.service.model.configuracao.Configuracao;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("SameReturnValue")
@Component
@Controller
@RequestMapping("configuracao/")
public class ConfiguracaoController implements ApplicationRunner {

    private static ConfiguracaoDAO component;
    private static List<Configuracao> configuracoes;

    final
    ConfiguracaoDAO configuracaoDAO;

    public ConfiguracaoController(ConfiguracaoDAO configuracaoDAO) {
        this.configuracaoDAO = configuracaoDAO;
    }
    /**
     *
     * @return retorna a configuração vigente em formato JSON a partir da data passada como parâmetro
     * @param dtAtual - Indica a vigência que será considerada.
     *
     * */
    @PostMapping("/getVigente")
    public static ResponseEntity<Configuracao> getVigenteJson(@RequestParam(value = "dtAtual") String dtAtual) throws ParseException {

        DateFormat formato = new SimpleDateFormat("dd-MM-yyyy");

        Collections.sort(configuracoes);

        for (Configuracao c : configuracoes) {

            if (!c.getVigencia().after(formato.parse(dtAtual)))
                return ResponseEntity.ok(c);

        }

        return null;
    }

    /**
     *
     * @return retorna a configuração vigente a partir da data passada como parâmetro
     * @param dtAtual - Indica a vigência que será considerada.
     *
     * */
    public static Configuracao getVigente(Date dtAtual) {

        Collections.sort(configuracoes);

        for (Configuracao c : configuracoes) {

            if (!c.getVigencia().after(dtAtual))
                return c;

        }

        return null;
    }


    @GetMapping("/index")
    @PreAuthorize("hasAuthority('APPROLE_qualidade')")
    public String index(Model model) {
        model.addAttribute("colaborador", new Configuracao());
        model.addAttribute("configuracoes", configuracaoDAO.findAll());
        return "configuracao/cadastro-configuracoes";
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('APPROLE_qualidade')")
    public String list(Model model) {
        model.addAttribute("configuracoes", configuracaoDAO.findAll());
        return "configuracao/list-configuracoes";
    }

    private static void loadConfiguracoes() {
        configuracoes = getConfiguracoes();
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('APPROLE_qualidade')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void add(@Valid ConfiguracaoDTO configuracaoDTO, BindingResult result) throws BindException {
        if (result.hasErrors()) {
            throw new BindException(result);
        }

        Configuracao c = new Configuracao();
        c.setDiasRev(configuracaoDTO.getDiasRev());
        c.setHorasDesAud(configuracaoDTO.getHorasDesAud());
        c.setHorasDesCsa(configuracaoDTO.getHorasDesCsa());
        c.setHorasDesRev(configuracaoDTO.getHorasDesRev());
        c.setPesoCsa(configuracaoDTO.getPesoCsa());
        c.setPrazoRac(configuracaoDTO.getPrazoRac());
        c.setVigencia(configuracaoDTO.getVigencia());
    
        configuracaoDAO.save(c);
        loadConfiguracoes();
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('APPROLE_qualidade')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) throws ClassNotFoundException {
        try {
            configuracaoDAO.delete(id);
            loadConfiguracoes();
        } catch (IllegalStateException | ConstraintViolationException ex) {

            throw new IllegalStateException("Configuração está em uso!", ex);

        } catch (EmptyResultDataAccessException e) {
            throw new ClassNotFoundException("Entidade não encontrada no servidor de dados!");
        }
    }

    private static List<Configuracao> getConfiguracoes() {
        return component.findAll();
    }

    @PostConstruct
    private void init() {
        component = this.configuracaoDAO;
    }

    @Override
    public void run(ApplicationArguments args) {
        loadConfiguracoes();
    }
}
