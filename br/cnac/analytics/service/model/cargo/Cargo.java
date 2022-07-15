package br.cnac.analytics.service.model.cargo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "cargos")
public class Cargo implements Comparable<Cargo>, Serializable {

    @Serial
    private static final long serialVersionUID = 1905122041950251207L;
    @Id
    private String cargoDesc;
    private String area;

    public Cargo() {
    }

    /**
     * @param cargoDesc - Descrição do cargo
     */
    public Cargo(String cargoDesc) {
        this.cargoDesc = cargoDesc;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCargoDesc() {
        return cargoDesc;
    }

    public void setCargoDesc(String cargoDesc) {
        this.cargoDesc = cargoDesc;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */

    @Override
    public int hashCode() {
        return Objects.hash(cargoDesc);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Cargo other)) {
            return false;
        }
        return Objects.equals(cargoDesc, other.cargoDesc);
    }

    @Override
    public int compareTo(Cargo o) {

        return this.cargoDesc.compareTo(o.cargoDesc);
    }

}
