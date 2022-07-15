package br.cnac.analytics.service.model.avaliacao;

import br.cnac.analytics.service.model.atividade.entity.Atividade;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "avaliacoes")
public class AvaliacaoCritica implements Serializable {

    @Serial
    private static final long serialVersionUID = 1905122041950251207L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "fk_cnpj_cliente", referencedColumnName = "fk_cnpj_cliente", nullable = false)
    @JoinColumn(name = "fk_num_escopo", referencedColumnName = "fk_num_escopo", nullable = false)
    @JoinColumn(name = "fk_ano_base", referencedColumnName = "anoBase", nullable = false)
    @JsonIgnoreProperties("avaliacaoCritica")
    private Atividade atividade;

    @Size(max = 1000)
    private String avaliacao;
    @Size(max = 1000)
    private String observacao;
    private Date dtEmissaoRAC;
    private Date dtEnvioRAC;

    private double nota;
    private int quantApontamento;
    private int quantFMR;
    private double ajusteTotal;

    private double vlrCarteira;
    private int qntOpMassificado;
    private double vlrOpMassificado;
    private double vlrAjustMassificado;
    private int qntOpIndividualizada;
    private double vlrOpIndividualizada;
    private double vlrAjustIndividualizada;

    @SuppressWarnings("unused")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }


    @SuppressWarnings("unused")
    public String getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(String avaliacaoCritica) {
        this.avaliacao = avaliacaoCritica;
    }

    @SuppressWarnings("unused")
    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    @SuppressWarnings("unused")
    public Date getDtEmissaoRAC() {
        return dtEmissaoRAC;
    }

    public void setDtEmissaoRAC(Date dtEmissaoRAC) {
        this.dtEmissaoRAC = dtEmissaoRAC;
    }

    @SuppressWarnings("unused")
    public Date getDtEnvioRAC() {
        return dtEnvioRAC;
    }

    public void setDtEnvioRAC(Date dtEnvioRAC) {
        this.dtEnvioRAC = dtEnvioRAC;
    }

    @SuppressWarnings("unused")
    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }

    @SuppressWarnings("unused")
    public int getQuantApontamento() {
        return quantApontamento;
    }

    public void setQuantApontamento(int quantApontamento) {
        this.quantApontamento = quantApontamento;
    }

    @SuppressWarnings("unused")
    public int getQuantFMR() {
        return quantFMR;
    }

    public void setQuantFMR(int quantFMR) {
        this.quantFMR = quantFMR;
    }

    @SuppressWarnings("unused")
    public double getAjusteTotal() {
        return ajusteTotal;
    }

    public void setAjusteTotal(double ajusteTotal) {
        this.ajusteTotal = ajusteTotal;
    }

    @SuppressWarnings("unused")
    public double getVlrCarteira() {
        return vlrCarteira;
    }

    public void setVlrCarteira(double vlrCarteira) {
        this.vlrCarteira = vlrCarteira;
    }

    @SuppressWarnings("unused")
    public int getQntOpMassificado() {
        return qntOpMassificado;
    }

    public void setQntOpMassificado(int qntOpMassificado) {
        this.qntOpMassificado = qntOpMassificado;
    }

    @SuppressWarnings("unused")
    public double getVlrOpMassificado() {
        return vlrOpMassificado;
    }

    public void setVlrOpMassificado(double vlrOpMassificado) {
        this.vlrOpMassificado = vlrOpMassificado;
    }

    @SuppressWarnings("unused")
    public double getVlrAjustMassificado() {
        return vlrAjustMassificado;
    }

    public void setVlrAjustMassificado(double vlrAjustMassificado) {
        this.vlrAjustMassificado = vlrAjustMassificado;
    }

    @SuppressWarnings("unused")
    public int getQntOpIndividualizada() {
        return qntOpIndividualizada;
    }

    public void setQntOpIndividualizada(int qntOpIndividualizada) {
        this.qntOpIndividualizada = qntOpIndividualizada;
    }

    @SuppressWarnings("unused")
    public double getVlrOpIndividualizada() {
        return vlrOpIndividualizada;
    }

    public void setVlrOpIndividualizada(double vlrOpIndividualizada) {
        this.vlrOpIndividualizada = vlrOpIndividualizada;
    }

    @SuppressWarnings("unused")
    public double getVlrAjustIndividualizada() {
        return vlrAjustIndividualizada;
    }

    public void setVlrAjustIndividualizada(double vlrAjustIndividualizada) {
        this.vlrAjustIndividualizada = vlrAjustIndividualizada;
    }
}
