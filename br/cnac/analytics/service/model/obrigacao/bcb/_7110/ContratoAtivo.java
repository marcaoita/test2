package br.cnac.analytics.service.model.obrigacao.bcb._7110;

import java.util.List;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ContratoAtivo {

    @JacksonXmlProperty(isAttribute = true)
    private String cnpj;

    @JacksonXmlProperty(localName = "escopoPadrao")
    private List<EscopoPadrao> escoposPadroes;


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
     * @return the escoposPadroes
     */
    @SuppressWarnings("unused")
    public List<EscopoPadrao> getEscoposPadroes() {
        return escoposPadroes;
    }

    /**
     * @param escoposPadroes the escoposPadroes to set
     */
    public void setEscoposPadroes(List<EscopoPadrao> escoposPadroes) {
        this.escoposPadroes = escoposPadroes;
    }

}
