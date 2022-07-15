package br.cnac.analytics.domain.dao.parecer;

import br.cnac.analytics.service.interfaces.parecer.ParecerRepository;
import br.cnac.analytics.service.model.parecer.Parecer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
@Repository
public class ParecerDAO {

    private final ParecerRepository parecerRepository;

    public ParecerDAO(ParecerRepository parecerRepository) {
        this.parecerRepository = parecerRepository;
    }

    public void save(Parecer parecer) {
        parecerRepository.save(parecer);
    }

    public List<Parecer> findAll() {
        return parecerRepository.findAll();
    }

    @SuppressWarnings("unused")
    public Parecer findOne(long id) {
        return parecerRepository.getReferenceById(id);
    }

    public void delete(long id) {

        parecerRepository.deleteById(id);
    }


}
