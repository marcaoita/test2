package br.cnac.analytics.domain.dto.avaliacao;

import br.cnac.analytics.service.model.atividade.chave_composta.PkAtividade;
import br.cnac.analytics.service.model.atividade.entity.Atividade;
import br.cnac.analytics.service.model.avaliacao.AvaliacaoCritica;

import java.sql.Date;

public class AvaliacaoCriticaDTO {

    private long id;

    private String cnpj;

    private String escopo;

    private String anoBase;

    private String avaliacao;
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

    public long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(long id) {
        this.id = id;
    }

    public String getAvaliacao() {
        return avaliacao;
    }

    @SuppressWarnings("unused")
    public void setAvaliacao(String avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getObservacao() {
        return observacao;
    }

    @SuppressWarnings("unused")
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Date getDtEmissaoRAC() {
        return dtEmissaoRAC;
    }

    @SuppressWarnings("unused")
    public void setDtEmissaoRAC(Date dtEmissaoRAC) {


        this.dtEmissaoRAC = dtEmissaoRAC;
    }

    public Date getDtEnvioRAC() {
        return dtEnvioRAC;
    }

    @SuppressWarnings("unused")
    public void setDtEnvioRAC(Date dtEnvioRAC) {

        this.dtEnvioRAC = dtEnvioRAC;
    }

    public double getNota() {
        return nota;
    }

    @SuppressWarnings("unused")
    public void setNota(double nota) {
        this.nota = nota;
    }

    public int getQuantApontamento() {
        return quantApontamento;
    }

    @SuppressWarnings("unused")
    public void setQuantApontamento(int quantApontamento) {
        this.quantApontamento = quantApontamento;
    }

    public int getQuantFMR() {
        return quantFMR;
    }

    @SuppressWarnings("unused")
    public void setQuantFMR(int quantFMR) {
        this.quantFMR = quantFMR;
    }

    public double getAjusteTotal() {
        return ajusteTotal;
    }

    @SuppressWarnings("unused")
    public void setAjusteTotal(double ajusteTotal) {
        this.ajusteTotal = ajusteTotal;
    }

    public double getVlrCarteira() {
        return vlrCarteira;
    }

    @SuppressWarnings("unused")
    public void setVlrCarteira(double vlrCarteira) {
        this.vlrCarteira = vlrCarteira;
    }

    public int getQntOpMassificado() {
        return qntOpMassificado;
    }

    @SuppressWarnings("unused")
    public void setQntOpMassificado(int qntOpMassificado) {
        this.qntOpMassificado = qntOpMassificado;
    }

    public double getVlrOpMassificado() {
        return vlrOpMassificado;
    }

    @SuppressWarnings("unused")
    public void setVlrOpMassificado(double vlrOpMassificado) {
        this.vlrOpMassificado = vlrOpMassificado;
    }

    public double getVlrAjustMassificado() {
        return vlrAjustMassificado;
    }

    @SuppressWarnings("unused")
    public void setVlrAjustMassificado(double vlrAjustMassificado) {
        this.vlrAjustMassificado = vlrAjustMassificado;
    }

    public int getQntOpIndividualizada() {
        return qntOpIndividualizada;
    }

    @SuppressWarnings("unused")
    public void setQntOpIndividualizada(int qntOpIndividualizada) {
        this.qntOpIndividualizada = qntOpIndividualizada;
    }

    public double getVlrOpIndividualizada() {
        return vlrOpIndividualizada;
    }

    @SuppressWarnings("unused")
    public void setVlrOpIndividualizada(double vlrOpIndividualizada) {
        this.vlrOpIndividualizada = vlrOpIndividualizada;
    }

    public double getVlrAjustIndividualizada() {
        return vlrAjustIndividualizada;
    }

    @SuppressWarnings("unused")
    public void setVlrAjustIndividualizada(double vlrAjustIndividualizada) {
        this.vlrAjustIndividualizada = vlrAjustIndividualizada;
    }

    public String getCnpj() {
        return cnpj;
    }

    @SuppressWarnings("unused")
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEscopo() {
        return escopo;
    }

    @SuppressWarnings("unused")
    public void setEscopo(String escopo) {
        this.escopo = escopo;
    }

    public String getAnoBase() {
        return anoBase;
    }

    @SuppressWarnings("unused")
    public void setAnoBase(String anoBase) {
        this.anoBase = anoBase;
    }

    public AvaliacaoCritica convertDTOToEntity() {

        AvaliacaoCritica a = new AvaliacaoCritica();

        a.setAvaliacao(this.getAvaliacao());
        a.setAjusteTotal(this.getAjusteTotal());
        a.setDtEmissaoRAC(this.getDtEmissaoRAC());
        a.setNota(this.getNota());
        a.setDtEnvioRAC(this.getDtEnvioRAC());
        a.setObservacao(this.getObservacao());
        PkAtividade pk = new PkAtividade(this.getCnpj(), this.getEscopo(), this.getAnoBase());
        a.setAtividade(new Atividade(pk));
        a.setQntOpIndividualizada(this.getQntOpIndividualizada());
        a.setId(this.getId());
        a.setQntOpMassificado(this.getQntOpMassificado());
        a.setQuantApontamento(this.getQuantApontamento());
        a.setQuantFMR(this.getQuantFMR());
        a.setVlrCarteira(this.getVlrCarteira());
        a.setVlrAjustIndividualizada(this.getVlrAjustIndividualizada());
        a.setVlrAjustMassificado(this.getVlrAjustMassificado());
        a.setVlrOpIndividualizada(this.getVlrOpIndividualizada());
        a.setVlrOpMassificado(this.getVlrOpMassificado());
        return a;
    }
}
