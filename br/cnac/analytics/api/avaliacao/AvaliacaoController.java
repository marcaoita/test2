package br.cnac.analytics.api.avaliacao;

import br.cnac.analytics.domain.dao.atividade.AtividadeDAO;
import br.cnac.analytics.domain.dao.avaliacao.AvaliacaoCriticaDAO;
import br.cnac.analytics.domain.dao.cliente.ClienteDAO;
import br.cnac.analytics.domain.dao.programacao.ProgramacaoDAO;
import br.cnac.analytics.domain.dto.avaliacao.AvaliacaoCriticaDTO;
import br.cnac.analytics.service.enumeration.TipoServico;
import br.cnac.analytics.service.model.atividade.chave_composta.PkAtividade;
import br.cnac.analytics.service.model.atividade.entity.Atividade;
import br.cnac.analytics.service.model.avaliacao.AvaliacaoCritica;
import br.cnac.analytics.service.model.cliente.Cliente;
import br.cnac.analytics.service.model.programacao.Programacao;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Transient;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings({"SameReturnValue", "SpringJavaInjectionPointsAutowiringInspection"})
@Controller
@RequestMapping("avaliacao/")
public class AvaliacaoController {

    final AvaliacaoCriticaDAO avaliacaoCriticaDAO;
    final ProgramacaoDAO programacaoDAO;
    final AtividadeDAO atividadeDao;
    final ClienteDAO clienteDAO;
    final EntityManager entityManager;


    public AvaliacaoController(AvaliacaoCriticaDAO avaliacaoCriticaDAO, ProgramacaoDAO programacaoDAO, AtividadeDAO atividadeDao, ClienteDAO clienteDAO, EntityManager entityManager) {
        this.avaliacaoCriticaDAO = avaliacaoCriticaDAO;
        this.programacaoDAO = programacaoDAO;
        this.atividadeDao = atividadeDao;
        this.clienteDAO = clienteDAO;
        this.entityManager = entityManager;
    }

    @PostMapping("/modal")
    public String modal(@RequestParam(value = "anoBase", required = false) String anoBase,
                        @RequestParam(value = "cnpjCliente", required = false) String cnpjCliente,
                        @RequestParam(value = "escopo", required = false) String escopo, Model model) {

        model.addAttribute("avaliacaoCriticaDTO", new AvaliacaoCriticaDTO());

            Cliente c = clienteDAO.findOne(cnpjCliente, anoBase);
            model.addAttribute("anoBase", anoBase);
            model.addAttribute("cliente", c);
            model.addAttribute("escopo", escopo);

            Set<Atividade> atividades = new HashSet<>();

            for (Programacao prog : c.getProgramacoes()) {
                atividades.addAll(prog.getAtividades()
                        .stream()
                        .filter(a -> a.getTipoServico().equals(TipoServico.AC))
                        .collect(Collectors.toSet()));
            }

            model.addAttribute("atividades", atividades);

        return "avaliacao/modal-avaliacao-critica";
    }

    @PostMapping("/list")
    public ResponseEntity<List<AvaliacaoCritica>> list() {
        return ResponseEntity.ok(avaliacaoCriticaDAO.findAll());
    }


    @PostMapping("/get")
    public String get(Model model, @RequestParam("cnpj") String cnpj, @RequestParam("anoBase") String anoBase, @RequestParam("escopo") String escopo) {

        Atividade a = atividadeDao.findOne(new PkAtividade(cnpj, escopo, anoBase));

        if (a.getAvaliacaoCritica() == null)
            a.setAvaliacaoCritica(new AvaliacaoCritica());

        model.addAttribute("avaliacao", a.getAvaliacaoCritica());

        return "avaliacao/avaliacoes";
    }


    @PostMapping("/add")
    @Transient
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void add(@Valid AvaliacaoCriticaDTO avaliacaoCriticaDTO, BindingResult result) throws BindException {

        if (result.hasErrors()) {
            throw new BindException(result);
        }

        AvaliacaoCritica avaliacaoCritica = avaliacaoCriticaDTO.convertDTOToEntity();

        Atividade a = entityManager.getReference(Atividade.class, avaliacaoCritica.getAtividade().getPkAtividade());

        a.setAvaliacaoCritica(avaliacaoCritica);
        avaliacaoCritica.setAtividade(a);

        avaliacaoCriticaDAO.save(avaliacaoCritica);
        atividadeDao.save(a);


    }

    @PostMapping("/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam("id") String id) throws ClassNotFoundException {
        try {
            avaliacaoCriticaDAO.delete(Long.parseLong(id));
        } catch (EmptyResultDataAccessException ex) {
            throw new ClassNotFoundException("Entidade não encontrada no servidor de dados!");
        }
    }

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void upload(@RequestParam("files") MultipartFile[] files) throws IOException {

        for (MultipartFile file : files) {

            try {

                avaliacaoCriticaDAO.uploadLote(file.getInputStream());

            } catch (IOException | PersistenceException e) {
                throw new IOException(e);
            } catch (CsvValidationException e) {
                e.printStackTrace();
            }
        }
    }

}
