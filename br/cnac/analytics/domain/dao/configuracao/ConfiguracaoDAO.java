package br.cnac.analytics.domain.dao.configuracao;

import br.cnac.analytics.service.model.configuracao.Configuracao;
import br.cnac.analytics.service.interfaces.configuracao.ConfiguracaoRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ConfiguracaoDAO {

    final
    ConfiguracaoRepository configuracaoRepository;

    public ConfiguracaoDAO(ConfiguracaoRepository configuracaoRepository) {
        this.configuracaoRepository = configuracaoRepository;
    }


    public void save(Configuracao c) {
        configuracaoRepository.save(c);
    }

    public List<Configuracao> findAll() {
        return configuracaoRepository.findAll();
    }

    public void delete(String id) {
        configuracaoRepository.deleteById(id);
    }

}
