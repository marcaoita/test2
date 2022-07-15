package br.cnac.analytics.service.interfaces.configuracao;

import org.springframework.data.jpa.repository.JpaRepository;

import br.cnac.analytics.service.model.configuracao.Configuracao;

public interface ConfiguracaoRepository extends JpaRepository<Configuracao, String> {

}
