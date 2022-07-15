package br.cnac.analytics.service.model.programacao;

import br.cnac.analytics.api.configuracao.ConfiguracaoController;
import br.cnac.analytics.api.feriado.FeriadoController;
import br.cnac.analytics.service.enumeration.TipoServico;
import br.cnac.analytics.service.model.atividade.entity.Atividade;
import br.cnac.analytics.service.model.cliente.Cliente;
import br.cnac.analytics.service.model.colaborador.Colaborador;
import br.cnac.analytics.service.model.configuracao.Configuracao;
import br.cnac.analytics.service.model.feriado.entity.Feriado;
import br.cnac.analytics.service.model.parecer.Parecer;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Pedro Belo
 */

@Entity
@Table(name = "programacoes")
@FilterDef(name = "tipoServicoFilterProg", parameters = @ParamDef(name = "tipoServico", type = "string"))
@FilterDef(name = "anoBaseFilterProg", parameters = @ParamDef(name = "anoBase", type = "string"))
@Filter(name = "tipoServicoFilterProg", condition = "tipo_servico in (:tipoServico)")
@Filter(name = "anoBaseFilterProg", condition = "ano_base = :anoBase")
public class Programacao implements Comparable<Programacao>, Serializable {

    @Serial
    private static final long serialVersionUID = 1905122041950251207L;

    @Transient
    private static final double DIA = 8.0;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToMany
    @CollectionTable(name = "colaboradores_alocados", joinColumns = @JoinColumn(name = "programacao_id"))
    @Fetch(FetchMode.SUBSELECT)
    @JsonIgnoreProperties({"programacoesGestores", "programacoes", "programacoesCSA"})
    private Set<Colaborador> colaboradores = new HashSet<>();
    @ManyToMany
    @CollectionTable(name = "gestores", joinColumns = @JoinColumn(name = "fk_programacao"))
    @Fetch(FetchMode.SUBSELECT)
    @JsonIgnoreProperties({"programacoesGestores", "programacoes", "programacoesCSA"})
    private Set<Colaborador> gestores = new HashSet<>();
    @ManyToMany
    @CollectionTable(name = "atividades_alocadas", joinColumns = @JoinColumn(name = "fk_programacao"))
    @Fetch(FetchMode.SUBSELECT)
    @JsonIgnore
    private List<Atividade> atividades;
    @ManyToOne
    @JoinColumn(name = "fk_cnpj_cliente")
    @JsonIgnoreProperties({"programacoes"})
    @Fetch(FetchMode.JOIN)
    private Cliente cli;
    @OneToOne(mappedBy = "programacao")
    @Fetch(FetchMode.JOIN)
    @JsonIgnoreProperties({"programacoes"})
    private Parecer parecer;
    @NotNull
    private Date dtInicio;
    private Date dtInicioExe;
    private Date dtFimExe;
    @NotNull
    private Date dtFim;
    @NotNull
    private String tipoVisita;
    @Enumerated(EnumType.STRING)
    private TipoServico tipoServico;
    private String dtBase;
    private String anoBase;
    @NotNull
    private int diasAtraso;
    @NotNull
    private String escritorioOrig;
    @ColumnDefault("1")
    private boolean temCSA;
    @ColumnDefault("0")
    private boolean amostraGerada;

    /**
     *
     */
    public Programacao() {
    }

    /**
     * @param id da programação.
     */
    public Programacao(long id) {
        this.id = id;
    }

    /**
     * @return Retorna a quantidade de membros da equipe, por padrão, somente
     * confederação e centrais contam o diretor na equipe.
     */
    @SuppressWarnings("unused")
    public int quantEquip() {

        String classe = this.cli.getClasse();

        if (!classe.equalsIgnoreCase("Confederação") && !classe.equalsIgnoreCase("Central"))
            return (int) (this.getColaboradores().size() + this.getGestores().stream()
                    .filter(g -> !g.getCargo().getArea().equals("Diretoria Executiva")).count());

        return (this.getColaboradores().size() + this.getGestores().size());

    }

    /**
     * @return the parecer
     */
    public Parecer getParecer() {
        return parecer;
    }

    /**
     * @param parecer the parecer to set
     */
    public void setParecer(Parecer parecer) {
        this.parecer = parecer;
    }

    /**
     * @return the amostraGerada
     */
    public boolean isAmostraGerada() {
        return amostraGerada;
    }

    /**
     * @param amostraGerada the amostraGerada to set
     */
    public void setAmostraGerada(boolean amostraGerada) {
        this.amostraGerada = amostraGerada;
    }

    /**
     * @return the temCSA
     */
    @SuppressWarnings("unused")
    public boolean isTemCSA() {
        return temCSA;
    }

    /**
     * @param temCSA the temCSA to set
     */
    public void setTemCSA(boolean temCSA) {
        this.temCSA = temCSA;
    }

    /**
     * @param gestores - CPF dos gestores.
     * @return Retorna TRUE se alguns dos gestores passados como parâmetro constarem
     * como gestores desta programação.
     */
    public boolean hasGestor(String[] gestores) {

        List<String> gestoresId = Arrays.asList(gestores);

        return this.gestores.stream().noneMatch(g -> gestoresId.contains(g.getCpfCnpj()));

    }

    /**
     * @return Retorna a média ponderada da revisão das atividades alocadas a esta programação.
     */
    @SuppressWarnings("unused")
    public String getEmRevisao() {

        double totalHorasVendidas = getHorasVendidas();
        double result = 0.0;

        for (Atividade a : atividades) {

            double peso = a.getHorasVendidas() / totalHorasVendidas;

            if (a.getMonitoramento() != null)
                result += a.getMonitoramento().getEmRevisao() * peso;

        }

        return convertToPercentage(result / 100);
    }

    /**
     * @return Retorna a média ponderada da revisão das atividades alocadas a esta programação.
     */
    @SuppressWarnings("unused")
    public String getRevisado() {

        double totalHorasVendidas = getHorasVendidas();
        double result = 0.0;

        for (Atividade a : atividades) {

            double peso = a.getHorasVendidas() / totalHorasVendidas;

            if (a.getMonitoramento() != null)
                result += a.getMonitoramento().getRevisado() * peso;

        }

        return convertToPercentage(result / 100);
    }


    /**
     * @return Retorna a média ponderada da execução das atividades alocadas a esta programação.
     */
    @SuppressWarnings("unused")
    public String getExecutado() {

        double totalHorasVendidas = getHorasVendidas();
        double result = 0.0;

        for (Atividade a : atividades) {

            double peso = a.getHorasVendidas() / totalHorasVendidas;

            if (a.getMonitoramento() != null)
                result += a.getMonitoramento().getExecutado() * peso;

        }

        return convertToPercentage(result / 100);
    }

    public Cliente getCli() {
        return cli;
    }

    public void setCli(Cliente cli) {
        this.cli = cli;
    }

    public Set<Colaborador> getGestores() {
        return gestores;
    }

    public void setGestores(Set<Colaborador> gestores) {
        this.gestores = gestores;
    }

    public String getAnoBase() {
        return anoBase;
    }

    public void setAnoBase(String anoBase) {
        this.anoBase = anoBase;
    }

    public Set<Colaborador> getColaboradores() {
        return this.colaboradores;
    }

    public void setColaboradores(Set<Colaborador> colaboradores) {

        if (colaboradores.isEmpty())
            throw new IllegalArgumentException("Lista de colaboradores alocados não pode ser vazia!");

        this.colaboradores = colaboradores;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Atividade> getAtividades() {
        return atividades;
    }

    public void setAtividades(List<Atividade> atividades) {
        this.atividades = atividades;
    }

    public double getHorasVendidas() {
        return this.atividades.stream().mapToDouble(Atividade::getHorasVendidas).sum();
    }

    @SuppressWarnings("unused")
    public double getDiasVendidos() {
        return Math.round(getHorasVendidas() / 8.0);
    }

    @SuppressWarnings("unused")
    public int getQuantEscopos() {
        return (int) this.atividades.stream().mapToInt(e -> e.getEscopo().hashCode()).count();
    }

    public String getStringEscopos() {

        return this.atividades.stream().map(a -> a.getEscopo().toString()).toList()
                + " - " + this.atividades.stream().map(Atividade::getDescricaoAmpliacaoEscopo).filter(Objects::nonNull).toList();
    }

    @SuppressWarnings("unused")
    public List<String> getEscopos() {
        return this.atividades.stream().map(a -> a.getEscopo().getNumEscopo()).toList();
    }

    @SuppressWarnings("unused")
    public String getStringColaboradores() {

        return this.colaboradores.toString();
    }

    @SuppressWarnings("unused")
    public String getDiretores(){
        return this.gestores.stream().filter(c -> c.getCargo().getArea().equals("Diretoria Executiva")).toList().toString();
    }

    @SuppressWarnings("unused")
    public String getSupervisores(){
        return this.gestores.stream().filter(c -> c.getCargo().getArea().equals("Supervisão")).toList().toString();
    }

    @SuppressWarnings("unused")
    public String getGerentes(){
        return this.gestores.stream().filter(c -> c.getCargo().getArea().equals("Gerência")).toList().toString();
    }

    private int getQuantAuditores() {

        return this.colaboradores.size();

    }

    /**
     * @return List - Retorna a lista de feriados aplicáveis a programação em
     * questão.
     */
    private List<Feriado> getFeriados() {

        List<Feriado> feriados = new ArrayList<>();

        for (Colaborador c : this.colaboradores) {
            feriados.addAll(FeriadoController
                    .findInterval(this.dtInicio, this.dtFim, c.getEstado(), c.getMunicipio()));
        }

        return feriados;
    }

    /**
     * @return Retorna as horas úteis (Descontando finais de semanas e feriados)
     * no intervalo de datas especificado na programação em questão.
     * Fórmula: (((diasUteis * DIA) * getQuantAuditores()) -
     * (horasDescontoFeriado))
     */
    private double getHorasUteis() {

        Calendar dataInicial = Calendar.getInstance();
        Calendar dtFinal = Calendar.getInstance();
        dataInicial.setTime(this.dtInicio);
        dtFinal.setTime(this.dtFim);

        long diasUteis = 0;
        double horasDescontoFeriado = getFeriados().size() * DIA;

        for (; !dataInicial.after(dtFinal); dataInicial.add(Calendar.DATE, 1)) {

            int diaDaSemana = dataInicial.get(Calendar.DAY_OF_WEEK);

            if (diaDaSemana != Calendar.SATURDAY && diaDaSemana != Calendar.SUNDAY)
                diasUteis++;

        }

        return ((diasUteis * DIA) * getQuantAuditores()) - (horasDescontoFeriado);
    }

    /**
     * @return Retorna as horas alocadas na programação, levando em consideração o
     * paralelismo de programações, quantidade de auditores e peso das
     * horas.
     * Fórmula: ((getHorasUteis() * peso) - descontoParalelismo)
     */
    public double getHorasAlocadas() {

        double descontoParalelismo = 0;
        double peso = 1;

        TipoServico tipo = this.tipoServico;

        if (tipo.equals(TipoServico.FERIAS) || tipo.equals(TipoServico.COMPENSACAO)
                || tipo.equals(TipoServico.TREINAMENTO))
            return 0;

        Set<Colaborador> colaboradoresParalelos = this.colaboradores.stream()
                .filter(c -> !c.getInfDescontoParalelo().isEmpty()).collect(Collectors.toSet());

        for (Colaborador c : colaboradoresParalelos) {
            for (InfProgParalela p : c.getInfDescontoParalelo()) {
                if (p.getIdProgramacao() == this.id)
                    descontoParalelismo += p.getHorasDesconto();
            }

        }

        if (this.tipoServico.equals(TipoServico.CSA)) {
            peso = Objects.requireNonNull(ConfiguracaoController.getVigente(this.getDtInicio())).getPesoCsa();

        }

        return (getHorasUteis() * peso) - descontoParalelismo;
    }

    @SuppressWarnings("unused")
    public double getDiasAlocados() {
        return Math.round(getHorasAlocadas() / 8.0);
    }

    /**
     * @return Retorna a data final da auditoria, considerando o fim da revisão.
     */
    public Date getDtFinalAuditoria() {

        int prazoFim = Objects.requireNonNull(ConfiguracaoController.getVigente(this.getDtInicio())).getDiasRev();

        prazoFim += this.diasAtraso; // dias de atraso são somados ao prazo final.

        Date dtFinalAudit = this.dtFim;

        return increDecreData(dtFinalAudit, prazoFim, new String[]{this.cli.getMunicipio()},
                new String[]{this.cli.getEstado()}, true, true);
    }

    /**
     * @return Retorna a data prevista de entrega das amostras.
     */
    public Date getDtPrevAmostra() {

        return increDecreData(this.dtInicio, this.cli.getDiasEnvAmostra(), new String[]{this.cli.getMunicipio()},
                new String[]{this.cli.getEstado()}, false, false);
    }

    public double getHorasDestCSA() {

        if (!this.temCSA)
            return 0.0;

        Configuracao c = ConfiguracaoController.getVigente(this.getDtInicio());

        double horasMaxima = 32.0;
        assert c != null;
        double horasCalculadas = ((getHorasVendidas() * c.getHorasDesAud()) * c.getHorasDesCsa());

        return Math.round(Math.min(horasMaxima, horasCalculadas));
    }

    @SuppressWarnings("unused")
    public double getSaldoLiquidoHoras() {
        return (getHorasVendidas() - (getHorasDestCSA() + getHorasAlocadas() + getHorasRevisao()));
    }

    @SuppressWarnings("unused")
    public double getDiasDesCSA() {
        return Math.round(getHorasDestCSA() / 8.0);
    }

    /**
     * @param dtInicio   - data de início.
     * @param diasMaximo - dias úteis máximo desejado para incrementar ou
     *                   decrementar.
     * @param municipios - Município para fins de feriado.
     * @param estados    - Unidade da federação para fins de feriados.
     * @param incrementa - informa se o método deverá incrementar (true) ou
     *                   decrementar (false) a data.
     * @return Retorna uma data incrementada ou decrementada levando em consideração
     * feriados e dias úteis.
     */
    private Date increDecreData(Date dtInicio, double diasMaximo, String[] municipios, String[] estados,
                                boolean incrementa, boolean consideraDiaUtil) {

        Calendar cDate = Calendar.getInstance();
        Calendar cDateFinal = Calendar.getInstance();

        cDate.setTime(dtInicio);

        int incremento = 1;

        if (!incrementa)
            incremento = -1;

        double diasUteis = 0;

        while (diasUteis < diasMaximo) {

            cDateFinal.setTime(cDate.getTime());

            int diaDaSemana = cDate.get(Calendar.DAY_OF_WEEK);

            if (consideraDiaUtil) {
                if (diaDaSemana != Calendar.SATURDAY && diaDaSemana != Calendar.SUNDAY
                        && FeriadoController.findByDate(new Date(cDate.getTime().getTime()), estados,
                        municipios).isEmpty())
                    diasUteis++;
            } else {
                diasUteis++;
            }

            cDate.add(Calendar.DATE, incremento);

        }

        return new Date(cDateFinal.getTime().getTime());
    }

    @SuppressWarnings("unused")
    public Date getDtEmissaoRAC() {

        int prazoRac = Objects.requireNonNull(ConfiguracaoController.getVigente(this.getDtInicio())).getPrazoRac();

        Date dtFinalAudit = getDtFinalAuditoria();

        return Date.valueOf(dtFinalAudit.toLocalDate().plusDays(prazoRac));

    }

    public double getHorasRevisao() {

        if (this.tipoServico.equals(TipoServico.CSA))
            return 0;

        double percHorasRev = Objects.requireNonNull(ConfiguracaoController.getVigente(this.getDtInicio())).getHorasDesRev();

        return (getHorasVendidas() * percHorasRev);
    }

    @SuppressWarnings("unused")
    public double getDiasRevisao() {
        return Math.round(getHorasRevisao() / 8.0);
    }

    public Date getDtInicio() {
        return dtInicio;
    }

    public void setDtInicio(Date dtInicio) {
        this.dtInicio = dtInicio;
    }

    @SuppressWarnings("unused")
    public Date getDtInicioExe() {
        return dtInicioExe;
    }

    public void setDtInicioExe(Date dtInicioExe) {
        this.dtInicioExe = dtInicioExe;
    }

    @SuppressWarnings("unused")
    public Date getDtFimExe() {
        return dtFimExe;
    }

    public void setDtFimExe(Date dtFimExe) {
        this.dtFimExe = dtFimExe;
    }

    public Date getDtFim() {
        return dtFim;
    }

    public void setDtFim(Date dtFim) {
        this.dtFim = dtFim;
    }

    @SuppressWarnings("unused")
    public String getTipoVisita() {
        return tipoVisita;
    }

    public void setTipoVisita(String tipoVisita) {
        this.tipoVisita = tipoVisita;
    }

    public TipoServico getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(TipoServico tipoServico) {
        this.tipoServico = tipoServico;
    }

    public String getDtBase() {
        return dtBase;
    }

    public void setDtBase(String dtBase) {
        this.dtBase = dtBase;
    }

    @SuppressWarnings("unused")
    public int getDiasAtraso() {
        return diasAtraso;
    }

    public void setDiasAtraso(int diasAtraso) {
        this.diasAtraso = diasAtraso;
    }

    @SuppressWarnings("unused")
    public String getEscritorioOrig() {
        return escritorioOrig;
    }

    public void setEscritorioOrig(String escritorioOrig) {
        this.escritorioOrig = escritorioOrig;
    }

    @Override
    public int compareTo(Programacao o) {

        if (this.dtInicio.before(o.getDtInicio()))
            return -1;

        if (this.dtInicio.after(o.getDtInicio()))
            return 1;

        return 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Programacao other)) {
            return false;
        }
        return id == other.id;
    }

    /**
     * @param valor valor em decimal que se deseja converter.
     * @return Retorna o valor passado como parâmetro no formato de porcentagem, por
     * exemplo, 0.8515 para 85.15%
     */
    private String convertToPercentage(double valor) {

        DecimalFormat df;

        df = new DecimalFormat("0%");

        return df.format(valor);
    }

}
