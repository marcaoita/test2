package br.cnac.analytics.service.model.obrigacao.bcb._7110;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public class Revisao {

    /**
     *
     */
    @SuppressWarnings("unused")
    public Revisao() {
        this.value = "";
    }

    @JacksonXmlProperty(isAttribute = true)
    private String inicioPrevisto;
    @JacksonXmlProperty(isAttribute = true)
    private String fimPrevisto;

    @JacksonXmlText
    private String value;

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
     * @return the inicioPrevisto
     */
    @SuppressWarnings("unused")
    public String getInicioPrevisto() {
        return inicioPrevisto;
    }

    /**
     * @param inicioPrevisto the inicioPrevisto to set
     */
    @SuppressWarnings("unused")
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
    @SuppressWarnings("unused")
    public void setFimPrevisto(String fimPrevisto) {
        this.fimPrevisto = fimPrevisto;
    }

}
