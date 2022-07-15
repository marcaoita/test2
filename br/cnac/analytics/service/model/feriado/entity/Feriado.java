package br.cnac.analytics.service.model.feriado.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import br.cnac.analytics.service.enumeration.TipoFeriado;

import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "feriados")
public class Feriado implements Comparable<Feriado> {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hilo_sequence_generator")
    private long id;

    @NotNull
    private Date dtRecesso;

    @NotNull
    private String nome;

    @Enumerated(EnumType.STRING)
    private TipoFeriado tipoFeriado;

    private String uf;

    private String municipio;


    public Date getDtRecesso() {
        return dtRecesso;
    }

    public void setDtRecesso(Date dtRecesso) {
        this.dtRecesso = dtRecesso;
    }

    @SuppressWarnings("unused")
    public TipoFeriado getTipoFeriado() {
        return tipoFeriado;
    }

    public void setTipoFeriado(TipoFeriado tipoFeriado) {
        this.tipoFeriado = tipoFeriado;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    @SuppressWarnings("unused")
    public long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(long id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Feriado feriados = (Feriado) o;
        return dtRecesso.equals(feriados.dtRecesso) && tipoFeriado == feriados.tipoFeriado;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dtRecesso, tipoFeriado);
    }

    @Override
    public int compareTo(Feriado o) {

        if (this.getDtRecesso().after(o.getDtRecesso()))
            return -1;

        if (this.getDtRecesso().before(o.getDtRecesso()))
            return 1;

        return 0;
    }

    @Override
    public String toString() {
        return this.getDtRecesso().toString() + "|" + this.uf + "|" + this.municipio;
    }
}
