package br.cnac.analytics.domain.dto.configuracao;

import java.sql.Date;

public class ConfiguracaoDTO {


    private double pesoCsa;
    private double horasDesCsa;
    private double horasDesRev;
    private double horasDesAud;
    private int diasRev;
    private int prazoRac;
    private Date vigencia;
    private int diasCSAIni;

    /**
     * @return the diasCSAIni
     */
    @SuppressWarnings("unused")
    public int getDiasCSAIni() {
        return diasCSAIni;
    }

    /**
     * @param diasCSAIni the diasCSAIni to set
     */
    @SuppressWarnings("unused")
    public void setDiasCSAIni(int diasCSAIni) {
        this.diasCSAIni = diasCSAIni;
    }

    /**
     * @return the pesoCsa
     */
    public double getPesoCsa() {
        return pesoCsa;
    }

    /**
     * @param pesoCsa the pesoCsa to set
     */
    @SuppressWarnings("unused")
    public void setPesoCsa(double pesoCsa) {
        this.pesoCsa = pesoCsa;
    }

    /**
     * @return the horasDesCsa
     */
    public double getHorasDesCsa() {
        return horasDesCsa;
    }

    /**
     * @param horasDesCsa the horasDesCsa to set
     */
    @SuppressWarnings("unused")
    public void setHorasDesCsa(double horasDesCsa) {
        this.horasDesCsa = horasDesCsa;
    }

    /**
     * @return the horasDesRev
     */
    public double getHorasDesRev() {
        return horasDesRev;
    }

    /**
     * @param horasDesRev the horasDesRev to set
     */
    @SuppressWarnings("unused")
    public void setHorasDesRev(double horasDesRev) {
        this.horasDesRev = horasDesRev;
    }

    /**
     * @return the horasDesAud
     */
    public double getHorasDesAud() {
        return horasDesAud;
    }

    /**
     * @param horasDesAud the horasDesAud to set
     */
    @SuppressWarnings("unused")
    public void setHorasDesAud(double horasDesAud) {
        this.horasDesAud = horasDesAud;
    }

    /**
     * @return the diasRev
     */
    public int getDiasRev() {
        return diasRev;
    }

    /**
     * @param diasRev the diasRev to set
     */
    @SuppressWarnings("unused")
    public void setDiasRev(int diasRev) {
        this.diasRev = diasRev;
    }

    /**
     * @return the prazoRac
     */
    public int getPrazoRac() {
        return prazoRac;
    }

    /**
     * @param prazoRac the prazoRac to set
     */
    @SuppressWarnings("unused")
    public void setPrazoRac(int prazoRac) {
        this.prazoRac = prazoRac;
    }

    /**
     * @return the vigencia
     */
    public Date getVigencia() {
        return vigencia;
    }

    /**
     * @param vigencia the vigencia to set
     */
    @SuppressWarnings("unused")
    public void setVigencia(Date vigencia) {
        this.vigencia = vigencia;
    }


}
