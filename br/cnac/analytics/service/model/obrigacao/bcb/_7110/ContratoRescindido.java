package br.cnac.analytics.service.model.obrigacao.bcb._7110;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ContratoRescindido {

    @JacksonXmlProperty(isAttribute = true)
    private String cnpj;
    @JacksonXmlProperty(isAttribute = true)
    private String dataRescisao;

    @JacksonXmlProperty(localName = "escopoPadrao")
    private List<EscopoPadrao> escoposPadrao;

    @JacksonXmlProperty(localName = "exameComplementar")
    private List<ExameComplementar> examesComplementares;


    /**
     * @return the examesComplementares
     */
    @SuppressWarnings("unused")
    public List<ExameComplementar> getExamesComplementares() {
        return examesComplementares;
    }

    /**
     * @param examesComplementares the examesComplementares to set
     */
    public void setExamesComplementares(List<ExameComplementar> examesComplementares) {
        this.examesComplementares = examesComplementares;
    }

    /**
     * @return the cnpj
     */
    @SuppressWarnings("unused")
    public String getCnpj() {
        return cnpj;
    }

    /**
     * @param cnpj the cnpj to set
     */
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    /**
     * @return the dataRescisao
     */
    @SuppressWarnings("unused")
    public String getDataRescisao() {
        return dataRescisao;
    }

    /**
     * @param dataRescisao the dataRescisao to set
     */
    public void setDataRescisao(String dataRescisao) {
        this.dataRescisao = dataRescisao;
    }

    /**
     * @return the escoposPadrao
     */
    @SuppressWarnings("unused")
    public List<EscopoPadrao> getEscoposPadrao() {
        return escoposPadrao;
    }

    /**
     * @param escoposPadrao the escoposPadrao to set
     */
    public void setEscoposPadrao(List<EscopoPadrao> escoposPadrao) {
        this.escoposPadrao = escoposPadrao;
    }


}
