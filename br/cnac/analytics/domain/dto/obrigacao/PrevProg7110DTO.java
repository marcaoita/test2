package br.cnac.analytics.domain.dto.obrigacao;

import javax.validation.constraints.NotNull;

public class PrevProg7110DTO {

    @NotNull
    private String dtBase;
    @NotNull
    private String anoBase;
    @NotNull
    private String tipoRemessa;
    @NotNull
    private String cpfResponsavel;
    @NotNull
    private String contemContrato;

    private double vlrHrAssociado;
    private double vlrHrNaoAssociado;

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
     * @return the anoBase
     */
    public String getAnoBase() {
        return anoBase;
    }

    /**
     * @param anoBase the anoBase to set
     */
    @SuppressWarnings("unused")
    public void setAnoBase(String anoBase) {
        this.anoBase = anoBase;
    }

    /**
     * @return the tipoRemessa
     */
    public String getTipoRemessa() {
        return tipoRemessa;
    }

    /**
     * @param tipoRemessa the tipoRemessa to set
     */
    @SuppressWarnings("unused")
    public void setTipoRemessa(String tipoRemessa) {
        this.tipoRemessa = tipoRemessa;
    }

    /**
     * @return the cpfResponsavel
     */
    public String getCpfResponsavel() {
        return cpfResponsavel;
    }

    /**
     * @param cpfResponsavel the cpfResponsavel to set
     */
    @SuppressWarnings("unused")
    public void setCpfResponsavel(String cpfResponsavel) {
        this.cpfResponsavel = cpfResponsavel;
    }

    /**
     * @return the contemContrato
     */
    public String getContemContrato() {
        return contemContrato;
    }

    /**
     * @param contemContrato the contemContrato to set
     */
    @SuppressWarnings("unused")
    public void setContemContrato(String contemContrato) {
        this.contemContrato = contemContrato;
    }

    /**
     * @return the vlrHrAssociado
     */
    public double getVlrHrAssociado() {
        return vlrHrAssociado;
    }

    /**
     * @param vlrHrAssociado the vlrHrAssociado to set
     */
    @SuppressWarnings("unused")
    public void setVlrHrAssociado(double vlrHrAssociado) {
        this.vlrHrAssociado = vlrHrAssociado;
    }

    /**
     * @return the vlrHrNaoAssociado
     */
    public double getVlrHrNaoAssociado() {
        return vlrHrNaoAssociado;
    }

    /**
     * @param vlrHrNaoAssociado the vlrHrNaoAssociado to set
     */
    @SuppressWarnings("unused")
    public void setVlrHrNaoAssociado(double vlrHrNaoAssociado) {
        this.vlrHrNaoAssociado = vlrHrNaoAssociado;
    }


}
