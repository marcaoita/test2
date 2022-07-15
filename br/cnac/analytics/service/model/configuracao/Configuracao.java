package br.cnac.analytics.service.model.configuracao;

import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "configuracoes")
public class Configuracao implements Comparable<Configuracao> {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    @NotNull
    private double pesoCsa;
    @NotNull
    private double horasDesCsa;
    @NotNull
    private double horasDesRev;
    @NotNull
    private double horasDesAud;
    @NotNull
    private int diasRev;
    @NotNull
    private int prazoRac;
    @NotNull
    private Date vigencia;

    @ColumnDefault("5")
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

    @SuppressWarnings("unused")
    public String getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(String id) {
        this.id = id;
    }

    public double getPesoCsa() {
        return pesoCsa;
    }

    public void setPesoCsa(double pesoCsa) {
        this.pesoCsa = pesoCsa;
    }

    public double getHorasDesCsa() {
        return horasDesCsa;
    }

    public void setHorasDesCsa(double horasDesCsa) {
        this.horasDesCsa = horasDesCsa;
    }

    public double getHorasDesRev() {
        return horasDesRev;
    }

    public void setHorasDesRev(double horasDesRev) {
        this.horasDesRev = horasDesRev;
    }

    public double getHorasDesAud() {
        return horasDesAud;
    }

    public void setHorasDesAud(double horasDesAud) {
        this.horasDesAud = horasDesAud;
    }

    public int getDiasRev() {
        return diasRev;
    }

    public void setDiasRev(int diasRev) {
        this.diasRev = diasRev;
    }

    public int getPrazoRac() {
        return prazoRac;
    }

    public void setPrazoRac(int prazoRac) {
        this.prazoRac = prazoRac;
    }

    public Date getVigencia() {
        return vigencia;
    }

    public void setVigencia(Date vigencia) {
        this.vigencia = vigencia;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */

    @Override
    public int hashCode() {
        return Objects.hash(id, vigencia);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Configuracao other)) {
            return false;
        }
        return Objects.equals(id, other.id) && Objects.equals(vigencia, other.vigencia);
    }

    @Override
    public int compareTo(Configuracao o) {

        if (this.getVigencia().after(o.getVigencia()))
            return -1;

        if (this.getVigencia().before(o.getVigencia()))
            return 1;

        return 0;
    }

}
