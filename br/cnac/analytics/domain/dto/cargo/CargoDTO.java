package br.cnac.analytics.domain.dto.cargo;

import br.cnac.analytics.service.model.cargo.Cargo;

public class CargoDTO {

    private String cargoDesc;
    private String area;

    /**
     * @return the cargoDesc
     */
    @SuppressWarnings("unused")
    public String getCargoDesc() {
        return cargoDesc;
    }

    /**
     * @param cargoDesc the cargoDesc to set
     */
    @SuppressWarnings("unused")
    public void setCargoDesc(String cargoDesc) {
        this.cargoDesc = cargoDesc;
    }

    /**
     * @return the area
     */
    @SuppressWarnings("unused")
    public String getArea() {
        return area;
    }

    /**
     * @param area the area to set
     */
    @SuppressWarnings("unused")
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * @return Retorna um objeto do tipo <code>Cargo</cargo>
     */
    public Cargo convertDTOToEntity() {

        Cargo cargo = new Cargo();
        cargo.setCargoDesc(this.cargoDesc);
        cargo.setArea(this.area);

        return cargo;
    }

}
