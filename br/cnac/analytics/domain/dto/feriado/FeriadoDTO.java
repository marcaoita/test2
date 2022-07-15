package br.cnac.analytics.domain.dto.feriado;

import java.sql.Date;

import br.cnac.analytics.service.enumeration.TipoFeriado;
import br.cnac.analytics.service.model.feriado.entity.Feriado;

public class FeriadoDTO {

    private Date dtRecesso;
    private String nome;
    private String tipoFeriado;
    private String uf;
    private String municipio;

    /**
     * @return the dtRecesso
     */
    public Date getDtRecesso() {
        return dtRecesso;
    }

    /**
     * @param dtRecesso the dtRecesso to set
     */
    @SuppressWarnings("unused")
    public void setDtRecesso(Date dtRecesso) {
        this.dtRecesso = dtRecesso;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    @SuppressWarnings("unused")
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the tipoFeriado
     */
    public String getTipoFeriado() {
        return tipoFeriado;
    }

    /**
     * @param tipoFeriado the tipoFeriado to set
     */
    @SuppressWarnings("unused")
    public void setTipoFeriado(String tipoFeriado) {
        this.tipoFeriado = tipoFeriado;
    }

    /**
     * @return the uf
     */
    public String getUf() {
        return uf;
    }

    /**
     * @param uf the uf to set
     */
    @SuppressWarnings("unused")
    public void setUf(String uf) {
        this.uf = uf;
    }

    /**
     * @return the municipio
     */
    public String getMunicipio() {
        return municipio;
    }

    /**
     * @param municipio the municipio to set
     */
    @SuppressWarnings("unused")
    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public Feriado convertDTOToEntity() {

        Feriado feriado = new Feriado();
        feriado.setDtRecesso(this.getDtRecesso());
        feriado.setMunicipio(this.getMunicipio());
        feriado.setNome(this.getNome());
        feriado.setTipoFeriado(TipoFeriado.valueOf(this.getTipoFeriado()));
        feriado.setUf(this.getUf());

        return feriado;
    }


}
