package br.cnac.analytics.domain.dao.cargo;

import br.cnac.analytics.service.interfaces.cargo.CargoRepository;
import br.cnac.analytics.service.model.cargo.Cargo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CargoDAO {

    private final CargoRepository cargoRepository;

    public CargoDAO(CargoRepository cargoRepository) {
        this.cargoRepository = cargoRepository;
    }

    public void save(Cargo cargo) {
        cargoRepository.save(cargo);
    }

    public List<Cargo> findAll() {
        return cargoRepository.findAll();
    }

    @SuppressWarnings("unused")
    public Cargo findOne(String idCargo) {
        return cargoRepository.getReferenceById(idCargo);
    }

    public void delete(String idCargo) {
        cargoRepository.deleteById(idCargo);
    }


}
