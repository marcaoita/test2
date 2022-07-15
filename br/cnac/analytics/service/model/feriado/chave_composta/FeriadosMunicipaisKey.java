package br.cnac.analytics.service.model.feriado.chave_composta;

import java.sql.Date;
import java.util.Comparator;
import java.util.Objects;

@SuppressWarnings("NullableProblems")
public class FeriadosMunicipaisKey implements Comparable<FeriadosMunicipaisKey> {

    private Date dtRecesso;
    private String uf;
    private String municipio;

    public FeriadosMunicipaisKey(Date dtRecesso, String uf, String municipio) {
        this.dtRecesso = dtRecesso;
        this.uf = uf;
        this.municipio = municipio;
    }

    public String getMunicipio() {
        return municipio;
    }

    @SuppressWarnings("unused")
    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public Date getDtRecesso() {
        return dtRecesso;
    }

    @SuppressWarnings("unused")
    public void setDtRecesso(Date dtRecesso) {
        this.dtRecesso = dtRecesso;
    }

    public String getUf() {
        return uf;
    }

    @SuppressWarnings("unused")
    public void setUf(String uf) {
        this.uf = uf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeriadosMunicipaisKey that = (FeriadosMunicipaisKey) o;
        return dtRecesso.equals(that.dtRecesso) && uf.equals(that.uf) && municipio.equals(that.municipio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dtRecesso, uf, municipio);
    }

    @Override
    public int compareTo(FeriadosMunicipaisKey o) {

        return Comparator
                .comparing(FeriadosMunicipaisKey::getUf)
                .thenComparing(FeriadosMunicipaisKey::getMunicipio)
                .thenComparing(FeriadosMunicipaisKey::getDtRecesso)
                .compare(this, o);

    }
}
