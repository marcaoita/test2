package br.cnac.analytics.service.model.obrigacao.bcb._7110;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public class EscopoPadrao {


    /**
     *
     */
    public EscopoPadrao() {
        this.value = "";
    }

    @JacksonXmlProperty(isAttribute = true)
    private String codigo;
    @JacksonXmlProperty(isAttribute = true)
    private String inicioPrevisto;
    @JacksonXmlProperty(isAttribute = true)
    private String fimPrevisto;
    @JacksonXmlProperty(isAttribute = true)
    private double horasPrevistas;
    @JacksonXmlProperty(isAttribute = true)
    private int quantidadeAuditores;
    @JacksonXmlProperty(isAttribute = true)
    private double faturamentoPrevisto;
    @JacksonXmlProperty(isAttribute = true)
    private String houveAmpliacaoEscopo;
    @JacksonXmlProperty(isAttribute = true)
    private String requisitanteAmpliacaoEscopo;
    @JacksonXmlProperty(isAttribute = true)
    private String houveSolicitacaoRevisao;

    @JacksonXmlText
    private String value;


    private Revisao revisao;

    /**
     * @return the value
     */
    @SuppressWarnings("unused")
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    @SuppressWarnings("unused")
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the codigo
     */
    @SuppressWarnings("unused")
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the inicioPrevisto
     */
    @SuppressWarnings("unused")
    public String getInicioPrevisto() {
        return inicioPrevisto;
    }

    /**
     * @param inicioPrevisto the inicioPrevisto to set
     */
    public void setInicioPrevisto(String inicioPrevisto) {
        this.inicioPrevisto = inicioPrevisto;
    }

    /**
     * @return the fimPrevisto
     */
    @SuppressWarnings("unused")
    public String getFimPrevisto() {
        return fimPrevisto;
    }

    /**
     * @param fimPrevisto the fimPrevisto to set
     */
    public void setFimPrevisto(String fimPrevisto) {
        this.fimPrevisto = fimPrevisto;
    }

    /**
     * @return the horasPrevistas
     */
    @SuppressWarnings("unused")
    public double getHorasPrevistas() {
        return horasPrevistas;
    }

    /**
     * @param horasPrevistas the horasPrevistas to set
     */
    public void setHorasPrevistas(double horasPrevistas) {
        this.horasPrevistas = horasPrevistas;
    }

    /**
     * @return the quantidadeAuditores
     */
    @SuppressWarnings("unused")
    public int getQuantidadeAuditores() {
        return quantidadeAuditores;
    }

    /**
     * @param quantidadeAuditores the quantidadeAuditores to set
     */
    public void setQuantidadeAuditores(int quantidadeAuditores) {
        this.quantidadeAuditores = quantidadeAuditores;
    }

    /**
     * @return the faturamentoPrevisto
     */
    @SuppressWarnings("unused")
    public double getFaturamentoPrevisto() {
        return faturamentoPrevisto;
    }

    /**
     * @param faturamentoPrevisto the faturamentoPrevisto to set
     */
    public void setFaturamentoPrevisto(double faturamentoPrevisto) {
        this.faturamentoPrevisto = faturamentoPrevisto;
    }

    /**
     * @return the houveAmpliacaoEscopo
     */
    @SuppressWarnings("unused")
    public String getHouveAmpliacaoEscopo() {
        return houveAmpliacaoEscopo;
    }

    /**
     * @param houveAmpliacaoEscopo the houveAmpliacaoEscopo to set
     */
    public void setHouveAmpliacaoEscopo(String houveAmpliacaoEscopo) {
        this.houveAmpliacaoEscopo = houveAmpliacaoEscopo;
    }

    /**
     * @return the requisitanteAmpliacaoEscopo
     */
    @SuppressWarnings("unused")
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
     * @return the houveSolicitacaoRevisao
     */
    public String getHouveSolicitacaoRevisao() {
        return houveSolicitacaoRevisao;
    }

    /**
     * @param houveSolicitacaoRevisao the houveSolicitacaoRevisao to set
     */
    public void setHouveSolicitacaoRevisao(String houveSolicitacaoRevisao) {
        this.houveSolicitacaoRevisao = houveSolicitacaoRevisao;
    }

    /**
     * @return the revisao
     */
    @SuppressWarnings("unused")
    public Revisao getRevisao() {
        return revisao;
    }

    /**
     * @param revisao the revisao to set
     */
    public void setRevisao(Revisao revisao) {
        this.revisao = revisao;
    }

}
