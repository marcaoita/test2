package br.cnac.analytics.service.interfaces.parecer;

import org.springframework.data.jpa.repository.JpaRepository;

import br.cnac.analytics.service.model.parecer.Parecer;

public interface ParecerRepository extends JpaRepository<Parecer, Long> {
    
}
