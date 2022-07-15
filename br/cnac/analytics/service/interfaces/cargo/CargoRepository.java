package br.cnac.analytics.service.interfaces.cargo;

import org.springframework.data.jpa.repository.JpaRepository;

import br.cnac.analytics.service.model.cargo.Cargo;

public interface CargoRepository extends JpaRepository<Cargo, String> {

}
