package br.cnac.analytics.service.interfaces.programacao;

import org.springframework.data.jpa.repository.JpaRepository;

import br.cnac.analytics.service.model.programacao.Programacao;

public interface ProgramacaoRepository extends JpaRepository<Programacao, Long> {

}
