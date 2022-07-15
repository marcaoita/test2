package br.cnac.analytics.service.interfaces.feriado;

import org.springframework.data.jpa.repository.JpaRepository;

import br.cnac.analytics.service.model.feriado.entity.Feriado;

public interface FeriadoRepository extends JpaRepository<Feriado, String> {

}
