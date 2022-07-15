package br.cnac.analytics.api.programacao.verificacao;

import br.cnac.analytics.api.cliente.ClienteController;
import br.cnac.analytics.api.configuracao.ConfiguracaoController;
import br.cnac.analytics.api.feriado.FeriadoController;
import br.cnac.analytics.domain.dao.cliente.ClienteDAO;
import br.cnac.analytics.domain.dao.colaborador.ColaboradorDAO;
import br.cnac.analytics.service.enumeration.TipoServico;
import br.cnac.analytics.service.model.atividade.entity.Atividade;
import br.cnac.analytics.service.model.cliente.Cliente;
import br.cnac.analytics.service.model.colaborador.Colaborador;
import br.cnac.analytics.service.model.configuracao.Configuracao;
import br.cnac.analytics.service.model.escopo.Escopo;
import br.cnac.analytics.service.model.feriado.entity.Feriado;
import br.cnac.analytics.service.model.programacao.Programacao;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.PostConstruct;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Pedro Belo
 * Classe responsável por fazer as devidas verificações ao se cadastrar
 * uma programação.
 */

@Controller
@RequestMapping("verifCadProg/")
@PreAuthorize("hasAnyAuthority('APPROLE_gestores', 'APPROLE_qualidade')")
public class VerificacaoCadProg {

    private static final long ID_PROGRAMACAO_TEMP = 99999999999L;
    private static final String ANO_BASE = "anoBase";
    private static ColaboradorDAO componentColaboradorDao;

    private static ClienteDAO componentClienteDAO;

    private final ClienteDAO clienteDAO;

    private final ColaboradorDAO colaboradorDAO;

    public VerificacaoCadProg(ClienteDAO clienteDAO, ColaboradorDAO colaboradorDAO) {
        this.clienteDAO = clienteDAO;
        this.colaboradorDAO = colaboradorDAO;
    }

    /**
     * @param cnpjCliente      - Cliente em questão
     * @param horas            - Horas que foram vendidas
     * @param dtInicio         - Data de início desejado.
     * @param anoBase          - Ano Base em questão.
     * @param tipoServico      - Tipo de serviço em questão.
     * @param colaboradoresIds - Colaboradores que serão alocados.
     * @param temCSA           - Informação se terá ou não a atuação do CSA no
     *                         cliente em questão.
     * @return Retorna um JSON com a data máxima que a programação poderá ter,
     * feriados aplicáveis ao intervalo, programações paralelas, férias ou
     * licenças, horas simuladas e horas já aplicadas.
     */
    @PostMapping("/dataMaxima")
    public static ResponseEntity<HashMap<String, Object>> getDataMaxima(
            @RequestParam(value = "cnpjCliente") String cnpjCliente,
            @RequestParam(value = "horas") double horas,
            @RequestParam(value = "dtInicio") String dtInicio,
            @RequestParam(value = ANO_BASE) String anoBase,
            @RequestParam(value = "tipoServico") String tipoServico,
            @RequestParam(value = "colaboradoresIds[]") String[] colaboradoresIds,
            @RequestParam(value = "temCSA") String temCSA,
            @RequestParam(value = "id", required = false) String id) {

        HashMap<String, Object> map = new HashMap<>();

        // Retorna o máximo de dias que poderá ter a programação.
        double diasMaximo = getDiasMaximo(horas, tipoServico, dtInicio, colaboradoresIds.length, temCSA);

        List<Colaborador> colaboradores = componentColaboradorDao.findAny(List.of(colaboradoresIds), anoBase);

        String dtFinalCalculada = getDtFinal(dtInicio, diasMaximo, colaboradores);

        map.put("dataFinal", dtFinalCalculada);
        map.put("feriadosAplicaveis", getFeriados(dtInicio, dtFinalCalculada, colaboradores));
        map.put("programacoesParalelas", getProgramacoesParalelas(dtInicio, dtFinalCalculada, colaboradores, id));
        map.put("feriasLicencas", getFeriasLicencas(colaboradores, dtInicio, dtFinalCalculada));
        map.put("horasSimuladas", getHorasSimuladas(dtInicio, dtFinalCalculada, tipoServico, horas, colaboradores, id));
        map.put("horasJaAlocadas", getHorasAlocadasClient(cnpjCliente, anoBase, tipoServico, id));
        map.put("programacaoDuplicada", getProgramacoesDuplicadas(cnpjCliente, dtInicio, dtFinalCalculada, anoBase, tipoServico));

        return ResponseEntity.ok(map);
    }

    /**
     * @param cnpjCliente      - Cnpj do cliente em questão.
     * @param dtInicio         - Data de início selecionada pelo usuário.
     * @param dtFim            - Data final selecionada pelo usuário.
     * @param anoBase          - Ano Base selecionado.
     * @param colaboradoresIds - Colaboradores que serão alocados.
     * @param tipoServico      - Tipo de serviço selecionado.
     * @param horas            - Horas Vendidas.
     * @return Retorna um JSON com as verificações do intervalo selecionado pelo
     * usuário, como feriados aplicáveis, programações paralelas,
     * horas simuladas, horas já alocadas e férias ou licenças no intervalo.
     */

    @PostMapping("/verificaIntervalo")
    public static ResponseEntity<HashMap<String, Object>> verificarIntervalo(
            @RequestParam(value = "cnpjCliente") String cnpjCliente,
            @RequestParam(value = "dtInicio") String dtInicio,
            @RequestParam(value = "dtFim") String dtFim,
            @RequestParam(value = "anoBase") String anoBase,
            @RequestParam(value = "colaboradoresIds[]") String[] colaboradoresIds,
            @RequestParam(value = "tipoServico") String tipoServico,
            @RequestParam(value = "horas") double horas,
            @RequestParam(value = "id", required = false) String id) {

        HashMap<String, Object> map = new HashMap<>();
        List<Colaborador> colaboradores = componentColaboradorDao.findAny(List.of(colaboradoresIds), anoBase);

        map.put("feriadosAplicaveis", getFeriados(dtInicio, dtFim, colaboradores));
        map.put("programacoesParalelas", getProgramacoesParalelas(dtInicio, dtFim, colaboradores, id));
        map.put("horasSimuladas", getHorasSimuladas(dtInicio, dtFim, tipoServico, horas, colaboradores, id));
        map.put("horasJaAlocadas", getHorasAlocadasClient(cnpjCliente, anoBase, tipoServico, id));
        map.put("feriasLicencas", getFeriasLicencas(colaboradores, dtInicio, dtFim));
        map.put("programacaoDuplicada", getProgramacoesDuplicadas(cnpjCliente, dtInicio, dtFim, anoBase, tipoServico));

        return ResponseEntity.ok(map);

    }

    /**
     * @param cnpj        CNPJ do cliente.
     * @param dtInicio    Data de início.
     * @param dtFim       Data fim
     * @param anoBase     Ano Base
     * @param tipoServico Tipo de serviço em questão.
     * @return Retorna programação duplicada, considera-se programação duplicada aquela em que o cliente e o mesmo e as datas de início e fim também.
     */
    private static List<Programacao> getProgramacoesDuplicadas(String cnpj, String dtInicio, String dtFim, String anoBase, String tipoServico) {

        //Para serviços não operacionais, tais como Férias, é permitido duplicatas.
        if (!TipoServico.getServicosOperacionais().contains(TipoServico.valueOf(tipoServico)))
            return Collections.emptyList();

        return componentClienteDAO.findOne(cnpj, anoBase).getProgramacoes()
                .stream()
                .filter(p ->
                        (p.getDtInicio().equals(stringToDate(dtInicio))
                                && p.getDtFim().equals(stringToDate(dtFim)))).toList();

    }

    /**
     * @param cnpj        da Cooperativa em questão.
     * @param anoBase     em questão.
     * @param tipoServico desejado.
     * @return - Retorna as horas alocadas de um cliente em função do tipo passado
     * como parâmetro.
     */
    private static double getHorasAlocadasClient(String cnpj, String anoBase, String tipoServico, String id) {

        Cliente cliente = ClienteController.getClient(cnpj, anoBase);

        if (id != null)
            cliente.getProgramacoes().removeIf(prog -> prog.getId() == Long.parseLong(id));


        return cliente.getProgramacoes()
                .stream()
                .filter(p -> p.getTipoServico()
                        .equals(TipoServico.valueOf(tipoServico)))
                .mapToDouble(Programacao::getHorasAlocadas)
                .sum();
    }

    /**
     * @param dtInicio         - Data de início da programação.
     * @param dtFim            - Data Fim da programação.
     * @param tipoServico      - Tipo de serviço em questão.
     * @param horasContratadas - Horas contratadas para a programação em questão.
     * @param colaboradores    - Colaboradores
     * @return Retorna as horas sumuladas pelo método. O método utiliza um objeto do
     * tipo programação com dados mínimos para fazer a simulação.
     */
    private static double getHorasSimuladas(String dtInicio, String dtFim, String tipoServico, double horasContratadas,
                                            List<Colaborador> colaboradores, String id) {

        //Serviços não operacionais não são elegíveis a cálculo de horas.
        if (!TipoServico.getServicosOperacionais().contains(TipoServico.valueOf(tipoServico)) && !TipoServico.valueOf(tipoServico).equals(TipoServico.CSA))
            return 0;

        Programacao p = new Programacao();
        Atividade a = new Atividade();

        if (id != null)
            colaboradores.forEach(c -> c.getProgramacoes().removeIf(prog -> prog.getId() == Long.parseLong(id)));

        a.setHorasVendidas(horasContratadas);
        a.setEscopo(new Escopo());
        p.setId(ID_PROGRAMACAO_TEMP);
        p.setColaboradores(new HashSet<>(colaboradores));
        p.setDtInicio(stringToDate(dtInicio));
        p.setDtFim(stringToDate(dtFim));
        p.setAtividades(Collections.singletonList(a));
        p.setTipoServico(TipoServico.valueOf(tipoServico));

        if (id == null)
            colaboradores.forEach(c -> c.getProgramacoes().add(p));

        return p.getHorasAlocadas();
    }

    /**
     * @param dtInicio      - Data de início da programação.
     * @param dtFim         - Data de fim da programação.
     * @param colaboradores - Colaboradores
     * @return Retorna as programações que estão em paralelo com outras.
     */
    private static Set<Programacao> getProgramacoesParalelas(String dtInicio, String dtFim,
                                                             List<Colaborador> colaboradores, String id) {

        if (id != null)
            colaboradores.forEach(c -> c.getProgramacoes().removeIf(prog -> prog.getId() == Long.parseLong(id)));

        Set<Programacao> programacoesParalelas = new HashSet<>();

        for (Colaborador c : colaboradores) {

            c.getProgramacoes().stream().filter(
                            p -> !p.getDtInicio().after(stringToDate(dtFim)) && !p.getDtFim().before(stringToDate(dtInicio)))
                    .forEach(programacoesParalelas::add);
        }

        return programacoesParalelas;

    }

    /**
     * @param colaboradores -Colaboradores
     * @param dtInicio      - Data de início que se deseja verificar.
     * @param dtFim         - Data de fim que se deseja verificar.
     * @return Retorna as férias ou licenças dos colaboradores em questão, no
     * intervalo passado como parâmetro.
     */
    private static Set<Programacao> getFeriasLicencas(List<Colaborador> colaboradores, String dtInicio, String dtFim) {

        Set<Programacao> feriasLicenca = new HashSet<>();

        for (Colaborador c : colaboradores) {

            c.getProgramacoes().stream().filter(p -> (TipoServico.getServicosAusencias().contains(p.getTipoServico()))
                            && (!p.getDtInicio().after(stringToDate(dtFim)) && !p.getDtFim().before(stringToDate(dtInicio))))
                    .forEach(feriasLicenca::add);

        }

        return feriasLicenca;

    }

    /**
     * @param dtInicio      - Data de início
     * @param dtFim         - Data final.
     * @param colaboradores - Colaboradores.
     * @return Retorna uma lista com os feriados aplicáveis ao intervalo e
     * colaboradores passados como parâmetro.
     */
    private static Set<Feriado> getFeriados(String dtInicio, String dtFim, List<Colaborador> colaboradores) {

        String[] municipios = colaboradores.stream().map(Colaborador::getMunicipio).toArray(String[]::new);
        String[] estados = colaboradores.stream().map(Colaborador::getEstado).toArray(String[]::new);

        return FeriadoController.findInterval(stringToDate(dtInicio), stringToDate(dtFim),
                estados, municipios);
    }

    /**
     * @param dtInicio      - Data inicial que servirá como base do cálculo
     * @param diasMaximo    - Máximo de dias úteis
     * @param colaboradores - Colaboradores que iram informar a cidade pertencente
     *                      para fins de feriado.
     * @return Método responsável por gerar uma data final dado uma data inicial e
     * dias máximos passado como parâmetro.
     */
    private static String getDtFinal(String dtInicio, double diasMaximo, List<Colaborador> colaboradores) {

        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cDate = Calendar.getInstance();
        Calendar cDateFinal = Calendar.getInstance();

        String[] municipios = colaboradores.stream().map(Colaborador::getMunicipio).toArray(String[]::new);
        String[] estados = colaboradores.stream().map(Colaborador::getEstado).toArray(String[]::new);

        cDate.setTime(Objects.requireNonNull(stringToDate(dtInicio)));

        double diasUteis = 0;

        while (diasUteis < diasMaximo) {

            cDateFinal.setTime(cDate.getTime());

            int diaDaSemana = cDate.get(Calendar.DAY_OF_WEEK);

            if (diaDaSemana != Calendar.SATURDAY && diaDaSemana != Calendar.SUNDAY
                    && FeriadoController.findByDate(new Date(cDate.getTime().getTime()), estados,
                    municipios).isEmpty())
                diasUteis++;

            cDate.add(Calendar.DATE, 1);

        }

        return dateFormat.format(cDateFinal.getTime());
    }

    /**
     * @param horasContratadas - Horas contratadas para as atividades selecionadas.
     * @param tipoServico      - Tipo de serviço em questão @see <code>TipoServico</code>
     * @param dtInicio         - Data de início.
     * @param quantColab       - Quantidade de colaboradores
     * @param temCSA           - Informa se a programação em questão terá suporte do CSA.
     * @return Retorna o máximo de dias possível de acordo com as horas e pesos do
     * tipo de serviço.
     */
    private static double getDiasMaximo(double horasContratadas,
                                        String tipoServico,
                                        String dtInicio,
                                        int quantColab,
                                        String temCSA) {

        final int DIA = 8;

        Configuracao config = ConfiguracaoController.getVigente(stringToDate(dtInicio));

        if (config == null)
            throw new IllegalArgumentException("Configuração vigente não encontrada!");

        double horasDestAudit;
        double horasAudit;
        double diasMaximo;

        if (tipoServico.equalsIgnoreCase("CSA")) {

            horasDestAudit = ((horasContratadas * config.getHorasDesAud()) * config.getHorasDesCsa());

            double horasMaxima = 32.0;

            horasDestAudit = Math.min(horasMaxima, horasDestAudit);


        } else if (temCSA.equalsIgnoreCase("true")) {
            horasAudit = (horasContratadas * config.getHorasDesAud());

            double horasMaxima = 32.0;
            double horasCSACalc = (horasAudit * config.getHorasDesCsa());

            horasDestAudit = horasAudit - Math.min(horasMaxima, horasCSACalc);

        } else {
            horasDestAudit = horasContratadas * (config.getHorasDesAud());
        }

        diasMaximo = ((horasDestAudit / quantColab) / DIA);

        if (Math.round(diasMaximo) < 1)
            return 1;

        return Math.round(diasMaximo);
    }

    /**
     * @param date - Data no formato DD-MM-YYYY a ser convertida.
     * @return Retorna um objeto do tipo date.
     */
    private static Date stringToDate(String date) {

        DateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
        try {
            return new Date(formato.parse(date).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostConstruct
    private void initColaborador() {
        componentColaboradorDao = this.colaboradorDAO;
        componentClienteDAO = this.clienteDAO;
    }

}
