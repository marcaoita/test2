package br.cnac.analytics.api.feriado;

import br.cnac.analytics.api.atividade.AtividadeController;
import br.cnac.analytics.api.cliente.ClienteController;
import br.cnac.analytics.domain.dao.feriado.FeriadoDAO;
import br.cnac.analytics.domain.dto.feriado.FeriadoDTO;
import br.cnac.analytics.domain.dto.filter.FilterDTO;
import br.cnac.analytics.service.enumeration.TipoFeriado;
import br.cnac.analytics.service.model.feriado.chave_composta.FeriadosEstaduaisKey;
import br.cnac.analytics.service.model.feriado.chave_composta.FeriadosMunicipaisKey;
import br.cnac.analytics.service.model.feriado.entity.Feriado;
import com.opencsv.exceptions.CsvValidationException;
import org.hibernate.Session;
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
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.validation.Valid;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings({"SameReturnValue", "SpringJavaInjectionPointsAutowiringInspection"})
@Component
@Controller
@RequestMapping("feriado/")
public class FeriadoController implements ApplicationRunner {

    private static EntityManager componentEntityManager;

    private final FeriadoDAO feriadoDAO;
    private static TreeMap<Date, Feriado> feriadosNacionais;
    private static TreeMap<FeriadosEstaduaisKey, Feriado> feriadosEstaduais;
    private static TreeMap<FeriadosMunicipaisKey, Feriado> feriadosMunicipais;
    private final EntityManager entityManager;

    public FeriadoController(FeriadoDAO feriadoDAO, EntityManager entityManager) {
        this.feriadoDAO = feriadoDAO;
        this.entityManager = entityManager;
    }

    /**
     *
     * @return Retorna feriados em um determinado intervalo, desconsiderando finais
     *         de semana.
     * @param dtInicial Data Inicial
     * @param dtFinal Data Final
     * @param estados em que os colaboradores estão dispostos.
     * @param municipios em que os colaboradores estão dispostos.
     *
     */
    public static Set<Feriado> findInterval(Date dtInicial, Date dtFinal, String[] estados,
                                            String[] municipios) {

        Set<Feriado> recessosSelecionados = new HashSet<>(feriadosNacionais.subMap(dtInicial, true, dtFinal, true).values());

        for (int i = 0; i < estados.length; i++) {
            recessosSelecionados.addAll(feriadosEstaduais.subMap(new FeriadosEstaduaisKey(dtInicial, estados[i]), true,
                    new FeriadosEstaduaisKey(dtFinal, estados[i]), true).values());

            recessosSelecionados.addAll(feriadosMunicipais.subMap(new FeriadosMunicipaisKey(dtInicial, estados[i], municipios[i]), true,
                    new FeriadosMunicipaisKey(dtFinal, estados[i], municipios[i]), true).values());
        }

        return recessosSelecionados;
    }

    /**
     *
     * @return Retorna feriados em um determinado intervalo, desconsiderando finais
     *         de semana.
     * @param dtInicial   Inicial
     * @param dtFinal   Final
     * @param estado em que o colaborador está disposto.
     * @param municipio Município em que o colaborador está disposto.
     *
     */
    public static Set<Feriado> findInterval(Date dtInicial, Date dtFinal, String estado,
                                            String municipio) {

        Set<Feriado> recessosSelecionados = new HashSet<>();

        recessosSelecionados.addAll(feriadosNacionais.subMap(dtInicial, true, dtFinal, true).values());

        recessosSelecionados.addAll(feriadosEstaduais.subMap(new FeriadosEstaduaisKey(dtInicial, estado), true,
                new FeriadosEstaduaisKey(dtFinal, estado), true).values());

        recessosSelecionados.addAll(feriadosMunicipais.subMap(new FeriadosMunicipaisKey(dtInicial, estado, municipio), true,
                new FeriadosMunicipaisKey(dtFinal, estado, municipio), true).values());

        return recessosSelecionados;
    }

    /**
     *
     * @return Retorna feriados em uma determinada data, desconsiderando finais de
     *         semana.
     * @param date    desejada
     * @param estados em que os colaboradores estão dispostos.
     * @param municipios em que os colaboradores estão dispostos.
     *
     */
    public static Set<Feriado> findByDate(Date date, String[] estados, String[] municipios) {

        Set<Feriado> recessosSelecionados = new HashSet<>();

        if (estados == null)
            return recessosSelecionados;

        Feriado feriadoNacional = feriadosNacionais.get(date);

        if (feriadoNacional != null)
            recessosSelecionados.add(feriadoNacional);

        for (int i = 0; i < estados.length; i++) {

            Feriado feriadoEstadual = feriadosEstaduais.get(new FeriadosEstaduaisKey(date, estados[i]));

            if (feriadoEstadual != null)
                recessosSelecionados.add(feriadoEstadual);

            Feriado feriadoMunicipal = feriadosMunicipais
                    .get(new FeriadosMunicipaisKey(date, estados[i], municipios[i]));

            if (feriadoMunicipal != null)
                recessosSelecionados.add(feriadoMunicipal);

        }

        return recessosSelecionados;
    }

    /**
     * Método estático responsável converter os feriados recebidos pelo banco de
     * dados em árvore binária.
     *
     */
    private static void loadMapsFeriados() {

        feriadosNacionais = new TreeMap<>(getFeriados(TipoFeriado.NACIONAL)
                .stream()
                .collect(
                        Collectors
                                .toMap(Feriado::getDtRecesso, f -> f)));

        feriadosMunicipais = new TreeMap<>(getFeriados(TipoFeriado.MUNICIPAL)
                .stream()
                .collect(
                        Collectors
                                .toMap(f -> new FeriadosMunicipaisKey(f.getDtRecesso(), f.getUf(), f.getMunicipio()),
                                        f -> f)));

        feriadosEstaduais = new TreeMap<>(getFeriados(TipoFeriado.ESTADUAL)
                .stream()
                .collect(
                        Collectors
                                .toMap(f -> new FeriadosEstaduaisKey(f.getDtRecesso(), f.getUf()), f -> f)));
    }

    public static List<Integer> anosDisponiveis() {

        Session session = componentEntityManager.unwrap(Session.class);

        return session.createQuery(
                "select year(f.dtRecesso) from Feriado f group by year(f.dtRecesso)",
                Integer.class).stream().sorted().toList();
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('APPROLE_qualidade')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void add(@Valid FeriadoDTO feriadoDTO, BindingResult result) throws BindException {

        if (result.hasErrors()) {
            throw new BindException(result);
        }

        feriadoDAO.save(feriadoDTO.convertDTOToEntity());
        loadMapsFeriados();
    }

    @GetMapping("/index")
    @PreAuthorize("hasAnyAuthority('APPROLE_gestaoPessoas', 'APPROLE_qualidade')")
    public String index(Model model) {
        model.addAttribute("feriado", new Feriado());
        model.addAttribute("anos", anosDisponiveis());
        return "feriado/cadastro-feriado";
    }

    @PostMapping("/list")
    @PreAuthorize("hasAnyAuthority('APPROLE_gestaoPessoas', 'APPROLE_qualidade')")
    public ResponseEntity<List<Feriado>> list(@Valid FilterDTO filterDTO) {

        Session session = componentEntityManager.unwrap(Session.class);

        if (filterDTO.getAnoBase().equals("selecione"))
            filterDTO.setAnoBase(AtividadeController.getBiggerYear().get(0));

        List<Feriado> feriados = session.createQuery(
                        "select f from Feriado f where year(f.dtRecesso) in (:anoBase)",
                        Feriado.class)
                .setParameterList("anoBase", Collections.singletonList(Integer.valueOf(filterDTO.getAnoBase())))
                .list();

        if (filterDTO.getMes() != null)
            feriados.removeIf(f -> !Arrays.asList(filterDTO.getMes()).contains(f.getDtRecesso().toLocalDate().getMonthValue()));

        return ResponseEntity.ok(feriados);
    }


    @PostMapping("/delete")
    @PreAuthorize("hasAuthority('APPROLE_qualidade')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam("id") String id) throws ClassNotFoundException {
        try {
            feriadoDAO.delete(id);
            loadMapsFeriados();
        } catch (EmptyResultDataAccessException ex) {
            throw new ClassNotFoundException("Entidade não encontrada no servidor de dados!");
        }
    }

    @PostMapping("/upload")
    @PreAuthorize("hasAuthority('APPROLE_qualidade')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void upload(@RequestParam("files") MultipartFile[] files) throws IOException, ParseException {

        for (MultipartFile file : files) {

            try {
                feriadoDAO.uploadLote(file.getInputStream());
                loadMapsFeriados();
            } catch (IOException e) {
                throw new IOException(e);
            } catch (ParseException e) {
                throw new ParseException("Erro ao converter data: ", e.getErrorOffset());
            } catch (CsvValidationException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return Retorna os feriados dos três anos precedentes do ano base mais atual
     *         do sistema,
     *         em função do tipo de feriado passado como parâmetros.
     * @param tipoFeriado - informa qual tipo de feriados devem ser retornado,
     *                    NACIONAL, ESTADUAL e MUNICIPAL.
     *
     */
    private static List<Feriado> getFeriados(TipoFeriado tipoFeriado) {

        Session session = componentEntityManager.unwrap(Session.class);

        return session.createQuery(
                "select f from Feriado f where (year(f.dtRecesso) in (:anoBase)) and (DATEPART(dw,f.dtRecesso) not in ('1','7')) and f.tipoFeriado = (:tipo)",
                Feriado.class)
                .setParameterList("anoBase",
                        AtividadeController.getYearsAvailable().stream().map(Integer::valueOf)
                                .toList())
                .setParameter("tipo", tipoFeriado).list()
                .stream().sorted(Feriado::compareTo)
                .toList();
    }

    @PostConstruct
    private void initEntityManager() {
        componentEntityManager = this.entityManager;
    }

    @Override
    public void run(ApplicationArguments args) {
        AtividadeController.loadYearsAvailable();
        loadMapsFeriados();
        ClienteController.loadClientsMap();
    }
}
