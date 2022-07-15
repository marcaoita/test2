package br.cnac.analytics.domain.dto.amostra;

import javax.validation.constraints.NotNull;

public class ControleAmostraDTO {

    @NotNull
    private String id;
    private String dtBase;
    private boolean amostraGerada;

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    @SuppressWarnings("unused")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the dtBase
     */
    public String getDtBase() {
        return dtBase;
    }

    /**
     * @param dtBase the dtBase to set
     */
    @SuppressWarnings("unused")
    public void setDtBase(String dtBase) {
        this.dtBase = dtBase;
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
    @SuppressWarnings("unused")
    public void setAmostraGerada(boolean amostraGerada) {
        this.amostraGerada = amostraGerada;
    }


}
