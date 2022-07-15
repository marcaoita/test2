package br.cnac.analytics.service.model.obrigacao.bcb._7110;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

@JacksonXmlRootElement(localName = "documento")
public class Documento {

    public Documento() {
        this.value = "";
    }

    @JacksonXmlProperty(isAttribute = true)
    private String codigoDocumento;
    @JacksonXmlProperty(isAttribute = true)
    private String cnpjAuditora;
    @JacksonXmlProperty(isAttribute = true)
    private String dataBase;
    @JacksonXmlProperty(isAttribute = true)
    private String anoReferencia;
    @JacksonXmlProperty(isAttribute = true)
    private String tipoRemessa;
    @JacksonXmlProperty(isAttribute = true)
    private String cpfResponsavel;
    @JacksonXmlProperty(isAttribute = true)
    private String contemContrato;

    @JacksonXmlProperty(localName = "contratoAtivo")
    private List<ContratoAtivo> contratosAtivos;

    @JacksonXmlProperty(localName = "contratoRescindido")
    private List<ContratoRescindido> contratosRescindidos;

    @JacksonXmlText
    private String value;

    /**
     * @return the contratosRescindidos
     */
    @SuppressWarnings("unused")
    public List<ContratoRescindido> getContratosRescindidos() {
        return contratosRescindidos;
    }


    /**
     * @param contratosRescindidos the contratosRescindidos to set
     */
    public void setContratosRescindidos(List<ContratoRescindido> contratosRescindidos) {
        this.contratosRescindidos = contratosRescindidos;
    }


    /**
     * @return the codigoDocumento
     */
    @SuppressWarnings("unused")
    public String getCodigoDocumento() {
        return codigoDocumento;
    }


    /**
     * @param codigoDocumento the codigoDocumento to set
     */
    public void setCodigoDocumento(String codigoDocumento) {
        this.codigoDocumento = codigoDocumento;
    }


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
     * @return the cnpjAuditora
     */
    @SuppressWarnings("unused")
    public String getCnpjAuditora() {
        return cnpjAuditora;
    }

    /**
     * @param cnpjAuditora the cnpjAuditora to set
     */
    public void setCnpjAuditora(String cnpjAuditora) {
        this.cnpjAuditora = cnpjAuditora;
    }

    /**
     * @return the dataBase
     */
    @SuppressWarnings("unused")
    public String getDataBase() {
        return dataBase;
    }

    /**
     * @param dataBase the dataBase to set
     */
    public void setDataBase(String dataBase) {
        this.dataBase = dataBase;
    }

    /**
     * @return the anoReferencia
     */
    @SuppressWarnings("unused")
    public String getAnoReferencia() {
        return anoReferencia;
    }

    /**
     * @param anoReferencia the anoReferencia to set
     */
    public void setAnoReferencia(String anoReferencia) {
        this.anoReferencia = anoReferencia;
    }

    /**
     * @return the tipoRemessa
     */
    @SuppressWarnings("unused")
    public String getTipoRemessa() {
        return tipoRemessa;
    }

    /**
     * @param tipoRemessa the tipoRemessa to set
     */
    public void setTipoRemessa(String tipoRemessa) {
        this.tipoRemessa = tipoRemessa;
    }

    /**
     * @return the cpfResponsavel
     */
    @SuppressWarnings("unused")
    public String getCpfResponsavel() {
        return cpfResponsavel;
    }

    /**
     * @param cpfResponsavel the cpfResponsavel to set
     */
    public void setCpfResponsavel(String cpfResponsavel) {
        this.cpfResponsavel = cpfResponsavel;
    }

    /**
     * @return the contemContrato
     */
    @SuppressWarnings("unused")
    public String getContemContrato() {
        return contemContrato;
    }

    /**
     * @param contemContrato the contemContrato to set
     */
    public void setContemContrato(String contemContrato) {
        this.contemContrato = contemContrato;
    }

    /**
     * @return the contratosAtivos
     */
    public List<ContratoAtivo> getContratosAtivos() {
        return contratosAtivos;
    }

    /**
     * @param contratosAtivos the contratosAtivos to set
     */
    public void setContratosAtivos(List<ContratoAtivo> contratosAtivos) {
        this.contratosAtivos = contratosAtivos;
    }


}
