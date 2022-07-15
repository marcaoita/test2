package br.cnac.analytics.domain.dao.avaliacao;

import br.cnac.analytics.service.interfaces.avaliacao.AvaliacaoCriticaRepository;
import br.cnac.analytics.service.interfaces.importacao_lote.ImportacaoLote;
import br.cnac.analytics.service.model.avaliacao.AvaliacaoCritica;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


@SuppressWarnings("RedundantThrows")
@Repository
public class AvaliacaoCriticaDAO implements ImportacaoLote {

    private final AvaliacaoCriticaRepository avaliacaoCriticaRepository;

    public AvaliacaoCriticaDAO(AvaliacaoCriticaRepository avaliacaoCriticaRepository) {
        this.avaliacaoCriticaRepository = avaliacaoCriticaRepository;
    }

    @SuppressWarnings("unused")
    public AvaliacaoCritica getById(long id) {
        return avaliacaoCriticaRepository.getReferenceById(id);
    }

    public List<AvaliacaoCritica> findAll() {
        return avaliacaoCriticaRepository.findAll();
    }

    public void save(AvaliacaoCritica a) {
        avaliacaoCriticaRepository.save(a);
    }

    public void delete(long id) {
        avaliacaoCriticaRepository.deleteById(id);
    }

    @Override
    public void uploadLote(InputStream file) throws IOException, CsvValidationException, PersistenceException {
        // Pendente de implementação.
    }

    @Override
    public ByteArrayOutputStream modeloCSV(String[] cabecalho) {
        return ImportacaoLote.super.modeloCSV(cabecalho);
    }

    @Override
    public ByteArrayOutputStream modeloCSV(String[] cabecalho, List<String[]> dadosExemplo) {
        return ImportacaoLote.super.modeloCSV(cabecalho, dadosExemplo);
    }
}
