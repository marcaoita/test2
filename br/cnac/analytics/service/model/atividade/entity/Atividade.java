package br.cnac.analytics.service.model.atividade.entity;

import br.cnac.analytics.service.enumeration.StatusAtividade;
import br.cnac.analytics.service.enumeration.TipoServico;
import br.cnac.analytics.service.model.atividade.chave_composta.PkAtividade;
import br.cnac.analytics.service.model.avaliacao.AvaliacaoCritica;
import br.cnac.analytics.service.model.cliente.Cliente;
import br.cnac.analytics.service.model.escopo.Escopo;
import br.cnac.analytics.service.model.monitoramento.Monitoramento;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.*;
import org.springframework.context.annotation.Lazy;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "atividades")
@FilterDef(name = "coopRecordFilter", parameters = @ParamDef(name = "coop", type = "string"))
@FilterDef(name = "anoBaseRecordFilter", parameters = @ParamDef(name = "anoBase", type = "string"))
@FilterDef(name = "tipoServicoRecordFilter", parameters = @ParamDef(name = "tipoServico", type = "string"))
@FilterDef(name = "statusRecordFilter", parameters = @ParamDef(name = "status", type = "string"))
@Filter(name = "coopRecordFilter", condition = "fk_cnpj_cliente = :coop")
@Filter(name = "anoBaseRecordFilter", condition = "ano_base = :anoBase")
@Filter(name = "tipoServicoRecordFilter", condition = "tipo_servico = :tipoServico")
@Filter(name = "statusRecordFilter", condition = "status_atividade in (:status)")
public class Atividade implements Serializable {

    @Serial
    private static final long serialVersionUID = 3782833083228374061L;

    /**
     * 
     */
    public Atividade() {
    }

    /**
     * @param pkAtividade - Chave composta da atividade.
     */
    public Atividade(PkAtividade pkAtividade) {
        this.pkAtividade = pkAtividade;
    }

    @EmbeddedId
    private PkAtividade pkAtividade;

    @MapsId("cnpjCliente")
    @JoinColumn(name = "fk_cnpj_cliente")
    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({ "programacoes" })
    @Fetch(FetchMode.JOIN)
    @Lazy
    private Cliente cliente;

    @MapsId("numEscopo")
    @JoinColumn(name = "fk_num_escopo")
    @OneToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @Lazy
    private Escopo escopo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_avaliacao")
    @Lazy
    @LazyToOne(LazyToOneOption.NO_PROXY)
    @JsonIgnoreProperties({"atividade"})
    private AvaliacaoCritica avaliacaoCritica;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "monitoramento_num_audit")
    @JsonIgnoreProperties("atividade")
    @Fetch(FetchMode.JOIN)
    @Lazy
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private Monitoramento monitoramento;

    @Size(min = 1, max = 255)
    @NotNull
    private String numContratoOrig;
    @NotNull
    private double horasVendidas;
    @Size(max = 4)
    @NotNull
    @Column(insertable = false, updatable = false)
    private String anoBase;
    
    @Enumerated(EnumType.STRING)
    private TipoServico tipoServico;

    @ColumnDefault("0")
    private boolean ampliacaoEscopo;

    private String requisitanteAmpliacaoEscopo;

    @Enumerated(EnumType.STRING)
    private StatusAtividade statusAtividade;

    private String descricaoAmpliacaoEscopo;

    private Date dtRescisao;

    public AvaliacaoCritica getAvaliacaoCritica() {
        return avaliacaoCritica;
    }

    public void setAvaliacaoCritica(AvaliacaoCritica avaliacaoCritica) {
        this.avaliacaoCritica = avaliacaoCritica;
    }

    public Monitoramento getMonitoramento() {
        return monitoramento;
    }

    public void setMonitoramento(Monitoramento monitoramento) {
        this.monitoramento = monitoramento;
    }

    public String getDescricaoAmpliacaoEscopo() {
        return descricaoAmpliacaoEscopo;
    }

    public void setDescricaoAmpliacaoEscopo(String descricaoAmpliacaoEscopo) {
        this.descricaoAmpliacaoEscopo = descricaoAmpliacaoEscopo;
    }

    /**
     * @return the dtRescisao
     */
    public Date getDtRescisao() {
        return dtRescisao;
    }

    /**
     * @param dtRescisao the dtRescisao to set
     */
    public void setDtRescisao(Date dtRescisao) {
        this.dtRescisao = dtRescisao;
    }

    /**
     * @return the requisitanteAmpliacaoEscopo
     */
    public String getRequisitanteAmpliacaoEscopo() {
        return requisitanteAmpliacaoEscopo;
    }

    /**
     * @param requisitanteAmpliacaoEscopo the requisitanteAmpliacaoEscopo to set
     */
    public void setRequisitanteAmpliacaoEscopo(String requisitanteAmpliacaoEscopo) {
        this.requisitanteAmpliacaoEscopo = requisitanteAmpliacaoEscopo;
    }

    /**
     * @return the ampliacaoEscopo
     */
    public boolean isAmpliacaoEscopo() {
        return ampliacaoEscopo;
    }

    /**
     * @param ampliacaoEscopo the ampliacaoEscopo to set
     */
    public void setAmpliacaoEscopo(boolean ampliacaoEscopo) {
        this.ampliacaoEscopo = ampliacaoEscopo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Escopo getEscopo() {
        return escopo;
    }

    public void setEscopo(Escopo escopo) {
        this.escopo = escopo;
    }

    public double getHorasVendidas() {
        return horasVendidas;
    }

    public void setHorasVendidas(double horasVendidas) {

        if (horasVendidas <= 0)
            throw new IllegalArgumentException("Horas vendidas não pode ser igual ou inferior a zero");

        this.horasVendidas = horasVendidas;

    }

    public String getAnoBase() {
        return anoBase;
    }

    public void setAnoBase(String anoBase) {

        if (anoBase.length() != 4)
            throw new IllegalArgumentException("Ano base inválido!");

        this.anoBase = anoBase;
    }

    public TipoServico getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(TipoServico tipoServico) {
        this.tipoServico = tipoServico;
    }

    public String getNumContratoOrig() {
        return numContratoOrig;
    }

    public void setNumContratoOrig(String numContratoOrig) {

        if (numContratoOrig.length() <= 1)
            throw new IllegalArgumentException("Número de Contrato de origem não pode ser nulo!");
        this.numContratoOrig = numContratoOrig;

    }

    public StatusAtividade getStatusAtividade() {
        return statusAtividade;
    }

    public void setStatusAtividade(StatusAtividade statusAtividade) {
        this.statusAtividade = statusAtividade;
    }

    public PkAtividade getPkAtividade() {
        return pkAtividade;
    }

    public void setPkAtividade(PkAtividade pkAtividade) {
        this.pkAtividade = pkAtividade;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cliente, escopo);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Atividade atividade = (Atividade) o;
        return Objects.equals(cliente, atividade.cliente) && Objects.equals(escopo, atividade.escopo) && Objects.equals(anoBase, atividade.anoBase);
    }
}
