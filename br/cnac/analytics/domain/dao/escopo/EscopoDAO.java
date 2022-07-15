package br.cnac.analytics.domain.dao.escopo;

import br.cnac.analytics.service.interfaces.escopo.EscopoRepository;
import br.cnac.analytics.service.model.escopo.Escopo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EscopoDAO {

    private final EscopoRepository escopoRepository;

    public EscopoDAO(EscopoRepository escopoRepository) {
        this.escopoRepository = escopoRepository;
    }

    public void save(Escopo escopo) {
        escopoRepository.save(escopo);
    }

    public List<Escopo> findAll() {
        return escopoRepository.findAll();
    }

    @SuppressWarnings("unused")
    public Escopo findOne(String numEscopo) {
        return escopoRepository.getReferenceById(numEscopo);
    }

    public void delete(String numEscopo) {

        escopoRepository.deleteById(numEscopo);
    }

}
