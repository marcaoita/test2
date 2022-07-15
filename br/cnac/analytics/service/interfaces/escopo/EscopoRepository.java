package br.cnac.analytics.service.interfaces.escopo;

import org.springframework.data.jpa.repository.JpaRepository;

import br.cnac.analytics.service.model.escopo.Escopo;


public interface EscopoRepository extends JpaRepository<Escopo, String> {

}
