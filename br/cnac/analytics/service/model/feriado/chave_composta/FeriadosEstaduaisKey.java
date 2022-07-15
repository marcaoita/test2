package br.cnac.analytics.service.model.feriado.chave_composta;

import java.sql.Date;
import java.util.Comparator;
import java.util.Objects;

@SuppressWarnings("NullableProblems")
public class FeriadosEstaduaisKey implements Comparable<FeriadosEstaduaisKey> {

    private Date dtRecesso;
    private String uf;

    public FeriadosEstaduaisKey(Date dtRecesso, String uf) {
        this.dtRecesso = dtRecesso;
        this.uf = uf;
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
    public int compareTo(FeriadosEstaduaisKey o) {

        return Comparator
                .comparing(FeriadosEstaduaisKey::getUf)
                .thenComparing(FeriadosEstaduaisKey::getDtRecesso)
                .compare(this, o);
    }


    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */

    @Override
    public int hashCode() {
        return Objects.hash(dtRecesso, uf);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FeriadosEstaduaisKey other)) {
            return false;
        }
        return Objects.equals(dtRecesso, other.dtRecesso) && Objects.equals(uf, other.uf);
    }


    @Override
    public String toString() {
        return this.hashCode() + " | ";
    }
}
