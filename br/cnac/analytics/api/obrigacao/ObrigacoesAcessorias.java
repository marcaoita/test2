package br.cnac.analytics.api.obrigacao;

import br.cnac.analytics.api.atividade.AtividadeController;
import br.cnac.analytics.domain.dao.atividade.AtividadeDAO;
import br.cnac.analytics.domain.dao.cliente.ClienteDAO;
import br.cnac.analytics.domain.dto.obrigacao.PrevProg7110DTO;
import br.cnac.analytics.service.enumeration.StatusAtividade;
import br.cnac.analytics.service.enumeration.TipoServico;
import br.cnac.analytics.service.model.atividade.entity.Atividade;
import br.cnac.analytics.service.model.cliente.Cliente;
import br.cnac.analytics.service.model.colaborador.Colaborador;
import br.cnac.analytics.service.model.obrigacao.bcb._7110.*;
import br.cnac.analytics.service.model.programacao.Programacao;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("obrigacoes-acessorias/")
@PreAuthorize("hasAuthority('APPROLE_qualidade')")
@SuppressWarnings({"unchecked", "SameReturnValue", "OptionalGetWithoutIsPresent"})
public class ObrigacoesAcessorias {

    private static final String ESCOPO_PADRAO = "escoposPadroes";
    private static final String EXAME_COMPLE = "examesComplementares";
    private static final String INICIO_PADRAO = "01-12-";
    private static final String FIM_PADRAO = "31-12-";

    final
    ClienteDAO clienteDAO;

    final
    AtividadeDAO atividadeDAO;

    public ObrigacoesAcessorias(ClienteDAO clienteDAO, AtividadeDAO atividadeDAO) {
        this.clienteDAO = clienteDAO;
        this.atividadeDAO = atividadeDAO;
    }

    /**
     * @param date - Data no formato DD-MM-YYYY a ser convertida.
     * @return Retorna um objeto do tipo <code>date</code>.
     * @throws ParseException - É lancada quando não é possível converter a data passada como parâmetro.
     */
    private static Date stringToDate(String date) throws ParseException {

        DateFormat formato = new SimpleDateFormat("dd-MM-yyyy");

        return new Date(formato.parse(date).getTime());
    }

    @GetMapping("/index")
    public String index(Model model) {

        model.addAttribute("doc7110", new PrevProg7110DTO());
        model.addAttribute("anoBase", AtividadeController.getYearsAvailable());
        return "obrigacao/bcb/7110";
    }

    @GetMapping("/7110")
    public void gera7110(HttpServletResponse response, @Valid PrevProg7110DTO prevProg7110DTO, BindingResult result)
            throws IOException, BindException, ParseException {

        if (result.hasErrors()) {
            throw new BindException(result);
        }

        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(module);
        xmlMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
        xmlMapper.setSerializationInclusion(Include.NON_NULL);
        response.setHeader("Content-Disposition", "attachment; filename=7110.xml");
        response.setContentType("text/xml");
        response.getOutputStream().write(xmlMapper.writeValueAsBytes(mapper(prevProg7110DTO)));
        response.flushBuffer();

    }

    /**
     * @param doc Cabeçalho do 7110
     * @return retorna o arquivo 7110
     * @throws ParseException - É lançado quando a conversão de datas falha.
     */
    private Documento mapper(PrevProg7110DTO doc) throws ParseException {

        List<Cliente> clientes = clienteDAO.findAllByAnoBase(doc.getAnoBase());

        Documento d = new Documento();

        d.setCodigoDocumento("7110");
        d.setCnpjAuditora("09140486");
        d.setDataBase(doc.getDtBase());
        d.setAnoReferencia(doc.getAnoBase());
        d.setTipoRemessa(doc.getTipoRemessa());
        d.setCpfResponsavel(doc.getCpfResponsavel());
        d.setContemContrato(doc.getContemContrato());

        List<ContratoAtivo> contratosAtivos = new ArrayList<>();

        for (Cliente c : clientes) {

            //Remove os tipos de serviços cujo tipo de serviço é diferente de AC.
            c.getProgramacoes().removeIf(p -> !p.getTipoServico().equals(TipoServico.AC));

            if (!c.getProgramacoes().isEmpty())
                contratosAtivos.add(getContrAtvByCliente(c, doc));
        }

        Map<String, Object> map = getContratosSuspensosCancelados(doc);
        d.setContratosAtivos(contratosAtivos);
        d.setContratosRescindidos((List<ContratoRescindido>) map.get("contratosRescindidos"));
        d.getContratosAtivos().addAll((Collection<? extends ContratoAtivo>) map.get("contratosSuspensos"));

        return d;
    }


    /***
     * @return  Retorna um Objeto do tipo <code>ContratoAtivo</code>
     * @param c Cliente
     * @param doc Cabeçalho 7110;
     *
     * */
    private ContratoAtivo getContrAtvByCliente(Cliente c, PrevProg7110DTO doc) throws ParseException {

        ContratoAtivo ca = new ContratoAtivo();

        ca.setCnpj(c.getCnpjCliente());

        Map<String, Object> map = getEscoposPadroes(c, doc);
        ca.setEscoposPadroes((List<EscopoPadrao>) map.get(ESCOPO_PADRAO));
        ca.setExamesComplementares((List<ExameComplementar>) map.get(EXAME_COMPLE));


        return ca;
    }

    /**
     * @return Retorna um MAP cujo a chave é o escopo Agregador da atividade e o valor são as programações alocadas a esta atividades.
     */
    private Map<String, Set<Programacao>> getMapAtividade(Set<Programacao> programacoes) {

        Map<String, Set<Programacao>> map = new HashMap<>();

        for (Programacao p : programacoes) {

            for (Atividade a : p.getAtividades()) {

                if (map.get(a.getEscopo().getEscopoAgregador()) == null) {
                    Set<Programacao> programacaoList = new HashSet<>();
                    programacaoList.add(p);
                    map.put(a.getEscopo().getEscopoAgregador(), programacaoList);
                } else {
                    map.get(a.getEscopo().getEscopoAgregador()).add(p);
                }
            }

        }
        return map;
    }

    private List<String> escoposComplementares() {
        return List.of("199", "299", "399");
    }

    /**
     * @param doc Cabeçalho 7110
     * @param c   Cliente.
     * @return Retorna um map de escopos padroes e complementares de um cliente.
     */
    private Map<String, Object> getEscoposPadroes(Cliente c, PrevProg7110DTO doc) throws ParseException {

        Map<String, Object> result = new HashMap<>();
        Map<String, Set<Programacao>> map = getMapAtividade(c.getProgramacoes());

        List<EscopoPadrao> escopos = new ArrayList<>();
        List<ExameComplementar> examesComplementares = new ArrayList<>();

        for (Map.Entry<String, Set<Programacao>> m : map.entrySet()) {

            if (!escoposComplementares().contains(m.getKey())) {
                escopos.add(getEscopoPadrao(m, doc));
            } else {
                examesComplementares.add(getExameComplementar(m, doc));
            }
        }

        result.put(ESCOPO_PADRAO, escopos);
        result.put(EXAME_COMPLE, examesComplementares);

        return result;
    }

    /**
     * @param m Map com as programações agrupadas por escopo agrupador.
     * @return Retorna um objeto do tipo <code>ExameComplementar</code>
     */
    private ExameComplementar getExameComplementar(Map.Entry<String, Set<Programacao>> m, PrevProg7110DTO doc) {

        ExameComplementar ec = new ExameComplementar();

        ec.setCodigo(m.getKey());
        ec.setNumero("01");

        ec.setInicioPrevisto(Objects.requireNonNull(m.getValue().stream().map(Programacao::getDtInicio)
                .min(Comparator.naturalOrder()).orElse(null)).toString());

        ec.setFimPrevisto(Objects.requireNonNull(m.getValue().stream().map(Programacao::getDtFinalAuditoria)
                .max(Comparator.naturalOrder()).orElse(null)).toString());

        ec.setHorasPrevistas(roundDouble(getHorasVendidasByProg(m.getValue(), m.getKey())));

        ec.setQuantidadeAuditores(getQuantEquipByProg(m.getValue()));

        double faturamento = getFaturamentoByProg(m.getKey(), m.getValue(), doc, m.getValue().stream().findFirst().get().getCli().isAssociado());
        ec.setFaturamentoPrevisto(roundDouble(faturamento));

        ec.setHouveSolicitacaoRevisao("N");

        return ec;
    }

    private Map<String, Object> getContratosSuspensosCancelados(PrevProg7110DTO doc) throws ParseException {

        Map<String, Object> result = new HashMap<>();
        List<ContratoRescindido> contratosRescindidos = new ArrayList<>();
        List<ContratoAtivo> contratosSuspensos = new ArrayList<>();
        Set<Atividade> atividades = getAtividadesByServico(doc.getAnoBase(), new String[]{StatusAtividade.SUSPENSA.toString(), StatusAtividade.CANCELADA.toString()});

        for (Map.Entry<Cliente, Set<Atividade>> m : getAtvByCli(atividades).entrySet()) {


            if (m.getValue().stream().findFirst().get().getStatusAtividade().equals(StatusAtividade.CANCELADA)) {
                contratosRescindidos.add(getContratoRescindido(m.getValue(), doc));
            } else {

                m.getKey().setProgramacoes(getProgSimuladas(m.getValue(), doc));
                contratosSuspensos.add(getContrAtvByCliente(m.getKey(), doc));
            }

        }

        result.put("contratosRescindidos", contratosRescindidos);
        result.put("contratosSuspensos", contratosSuspensos);
        return result;
    }


    /**
     * @param atividades lista de atividades.
     * @return Retora um Map das atividades agrupadas por cliente.
     */
    private Map<Cliente, Set<Atividade>> getAtvByCli(Set<Atividade> atividades) {

        return atividades.stream()
                .collect(Collectors.groupingBy(Atividade::getCliente, Collectors.toSet()));

    }

    /**
     * @param anoBase   - anoBase em questão.
     * @param statusSrv - Array contento os status das atividades que se deseja
     *                  filtrar.
     * @return Retorna uma lista com as atividades, com base no tipo de serviço e
     * anoBase passados como parâmetro.
     */
    private Set<Atividade> getAtividadesByServico(String anoBase, String[] statusSrv) {

        return atividadeDAO
                .findAll(anoBase, statusSrv)
                .stream()
                .filter(a -> a.getTipoServico().equals(TipoServico.AC))
                .collect(Collectors.toSet());

    }

    private ContratoRescindido getContratoRescindido(Set<Atividade> atividades, PrevProg7110DTO doc) throws ParseException {

        ContratoRescindido contratoRescindido = new ContratoRescindido();


        atividades.stream().findFirst().ifPresent(atv -> contratoRescindido.setCnpj(atv.getCliente().getCnpjCliente()));
        atividades.stream().findFirst().ifPresent(a -> contratoRescindido.setDataRescisao(a.getDtRescisao().toString()));

        Cliente c = atividades.stream().findFirst().get().getCliente();
        c.setProgramacoes(getProgSimuladas(atividades, doc));

        Map<String, Object> map = getEscoposPadroes(c, doc);
        contratoRescindido.setEscoposPadrao((List<EscopoPadrao>) map.get(ESCOPO_PADRAO));
        contratoRescindido.setExamesComplementares((List<ExameComplementar>) map.get(EXAME_COMPLE));

        return contratoRescindido;
    }

    /**
     * Contratos rescindidos e suspensos não possuem programação, logo, para fins de 7110 é gerado uma programação com dados simples e datas fixas.
     *
     * @param atividades Atividades do cliente em questão.
     * @param doc        Cabeçalho 7110
     * @return Retorna uma lista de programações com dados simples.
     */
    private Set<Programacao> getProgSimuladas(Set<Atividade> atividades, PrevProg7110DTO doc) throws ParseException {

        // por padrão, contratos rescindido ou suspensos recebem datas fixas, entre 01 e
        // 31 de dezembro do ano Base
        String dtInicioPadrao = INICIO_PADRAO + doc.getAnoBase();
        String dtFimPadrao = FIM_PADRAO + doc.getAnoBase();

        Set<Programacao> programacoes = new HashSet<>();

        long idProgTemp = 999L; //ID temporário da programação.

        //Para contratos rescindidos, não há programações, logo simulo programação com dados básicos.
        for (Atividade a : atividades) {

            idProgTemp++;

            Programacao p = new Programacao();
            p.setId(idProgTemp);
            p.setTipoServico(TipoServico.AC);
            p.setAtividades(List.of(a));
            p.setCli(a.getCliente());
            p.setDtInicio(stringToDate(dtInicioPadrao));
            p.setDtFim(stringToDate(dtFimPadrao));

            programacoes.add(p);

        }

        return programacoes;
    }

    private EscopoPadrao getEscopoPadrao(Map.Entry<String, Set<Programacao>> m, PrevProg7110DTO doc) throws ParseException {

        EscopoPadrao ep = new EscopoPadrao();

        String dtFimPadrao = FIM_PADRAO + doc.getAnoBase();

        ep.setCodigo(m.getKey());

        ep.setInicioPrevisto(Objects.requireNonNull(m.getValue().stream().map(Programacao::getDtInicio)
                .min(Comparator.naturalOrder()).orElse(null)).toString());


        //Por padrão, a data de fim não pode ultrapassar 31-12 do ano Base.
        Date dtFimObtida = Objects.requireNonNull(m.getValue().stream().map(Programacao::getDtFinalAuditoria)
                .max(Comparator.naturalOrder()).orElse(null));

        Date dtFimCalc = new Date(stringToDate(dtFimPadrao).getTime());

        if (dtFimObtida.after(dtFimCalc)) {
            ep.setFimPrevisto(dtFimCalc.toString());
        } else {
            ep.setFimPrevisto(dtFimObtida.toString());
        }

        ep.setHorasPrevistas(roundDouble(getHorasVendidasByProg(m.getValue(), m.getKey())));

        ep.setQuantidadeAuditores(getQuantEquipByProg(m.getValue()));

        double faturamento = getFaturamentoByProg(m.getKey(), m.getValue(), doc, m.getValue().stream().findFirst().get().getCli().isAssociado());
        ep.setFaturamentoPrevisto(roundDouble(faturamento));

        String requisitanteAmpli = getRequisitanteAmplEscopoByProg(m.getValue());
        ep.setHouveAmpliacaoEscopo(requisitanteAmpli == null ? "N" : "S");

        if (requisitanteAmpli != null)
            ep.setRequisitanteAmpliacaoEscopo(requisitanteAmpli);

        ep.setHouveSolicitacaoRevisao("N");

        if (ep.getHouveSolicitacaoRevisao().equals("S"))
            ep.setRevisao(null);

        return ep;
    }


    /**
     * @param programacoes Programações.
     * @return Retorna o requisitante de ampliação de escopo caso haja ampliação, retorna null se não houver.
     */
    private String getRequisitanteAmplEscopoByProg(Set<Programacao> programacoes) {

        Set<Atividade> atividades = new HashSet<>();
        final String[] result = {null};

        for (Programacao p : programacoes) {
            atividades.addAll(p.getAtividades());
        }

        atividades.stream().filter(Atividade::isAmpliacaoEscopo).findFirst().ifPresent(
                a -> result[0] = a.getRequisitanteAmpliacaoEscopo()
        );

        return result[0];
    }


    /**
     * @param valor Valor a ser arredondado.
     * @return Retorna um double com até 3 casas decimais.
     */
    private double roundDouble(double valor) {
        return BigDecimal.valueOf(valor).setScale(3, RoundingMode.HALF_EVEN).doubleValue();
    }


    /**
     * @param programacoes Programações
     * @return Retorna a quantidade de equipes alocadas as programações. Retorna um numero fixo 4 caso alguma atividade esteja suspensa ou cancelada.
     */
    private int getQuantEquipByProg(Set<Programacao> programacoes) {

        Set<Colaborador> colaboradores = new HashSet<>();
        List<String> status = List.of("SUSPENSA", "CANCELADA");
        List<String> segmentacoes = List.of("S4", "S3");

        for (Programacao p : programacoes) {

            if (status.contains(p.getAtividades().stream().findFirst().get().getStatusAtividade().toString()))
                return 4;

            colaboradores.addAll(p.getColaboradores());
            colaboradores.addAll(p.getGestores());
        }


        //Por padrão, os diretores não são contados na equipe, exceto em clientes cujo segmentação é S4 ou S3.
        if (segmentacoes.contains(programacoes.stream().findFirst().get().getCli().getSegmentacao()))
            colaboradores.removeIf(c -> c.getCargo().getArea().equals("Diretoria"));

        return colaboradores.size();
    }

    /**
     * @param programacoes Programações.
     * @param escopo       Escopo Agrupador.
     * @return Retorna a soma das horas vendidas de um determinado escopo agrupador. Suprimindo duplicatas.
     */
    private double getHorasVendidasByProg(Set<Programacao> programacoes, String escopo) {

        Set<Atividade> atividades = new HashSet<>();

        for (Programacao p : programacoes) {
            atividades.addAll(p.getAtividades());
        }

        return atividades.stream().filter(a -> a.getEscopo().getEscopoAgregador().equals(escopo)).mapToDouble(Atividade::getHorasVendidas).sum();
    }

    /**
     * @param doc          Cabeçalho do 7110
     * @param escopo       Escopo Agrupador
     * @param programacoes Lista de programações.
     * @param isAssociado  É associado.
     * @return Retorna o valor do faturamento.
     */
    private double getFaturamentoByProg(String escopo, Set<Programacao> programacoes, PrevProg7110DTO doc, boolean isAssociado) {

        double faturamento;
        double horasVendidas = getHorasVendidasByProg(programacoes, escopo);

        if (isAssociado) {
            faturamento = doc.getVlrHrAssociado()
                    * horasVendidas;
        } else {
            faturamento = doc.getVlrHrNaoAssociado()
                    * horasVendidas;
        }

        return faturamento;
    }

}
