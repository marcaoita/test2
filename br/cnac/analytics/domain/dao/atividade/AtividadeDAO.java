package br.cnac.analytics.domain.dao.atividade;

import br.cnac.analytics.api.atividade.AtividadeController;
import br.cnac.analytics.service.enumeration.StatusAtividade;
import br.cnac.analytics.service.enumeration.TipoServico;
import br.cnac.analytics.service.interfaces.atividade.AtividadeRepository;
import br.cnac.analytics.service.interfaces.atividade.AtividadeSimple;
import br.cnac.analytics.service.interfaces.importacao_lote.ImportacaoLote;
import br.cnac.analytics.service.model.atividade.chave_composta.PkAtividade;
import br.cnac.analytics.service.model.atividade.entity.Atividade;
import br.cnac.analytics.service.model.cliente.Cliente;
import br.cnac.analytics.service.model.escopo.Escopo;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Repository
public class AtividadeDAO implements ImportacaoLote {

    /**
     * Definição de filtros da entidade
     */
    private static final String ANO_BASE = "anoBase";
    private static final String COOPERATIVA = "coop";
    private static final String TIPO_SERVICO = "tipoServico";
    private static final String[] FILTERS_DEF = {"anoBaseRecordFilter", "coopRecordFilter", "tipoServicoRecordFilter", "statusRecordFilter"};

    private static final String QUERY = "select A from Atividade as A join fetch A.cliente join fetch A.escopo  left join fetch A.avaliacaoCritica left join fetch A.monitoramento";
    private final AtividadeRepository atividadeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public AtividadeDAO(AtividadeRepository atividadeRepository) {
        this.atividadeRepository = atividadeRepository;
    }

    public void save(Atividade atividade) {
        atividadeRepository.save(atividade);
    }

    public List<Atividade> findAll(String anoBase, String cnpj, String tipoServico) {

        Session session = entityManager.unwrap(Session.class);

        if (anoBase == null)
            session.enableFilter(FILTERS_DEF[0]).setParameter(ANO_BASE,
                    AtividadeController.getBiggerYear().get(0));

        if (anoBase != null)
            session.enableFilter(FILTERS_DEF[0]).setParameter(ANO_BASE, anoBase);

        if (cnpj != null)
            session.enableFilter(FILTERS_DEF[1]).setParameter(COOPERATIVA, cnpj);

        if (tipoServico != null)
            session.enableFilter(FILTERS_DEF[2]).setParameter(TIPO_SERVICO, tipoServico);

        return entityManager.createQuery(QUERY, Atividade.class).getResultList();
    }


    public List<Atividade> findAll(String anoBase, String[] status) {

        Session session = entityManager.unwrap(Session.class);

        if (anoBase == null)
            session.enableFilter(FILTERS_DEF[0]).setParameter(ANO_BASE,
                    AtividadeController.getBiggerYear().get(0));

        if (anoBase != null)
            session.enableFilter(FILTERS_DEF[0]).setParameter(ANO_BASE, anoBase);

        if (status != null)
            session.enableFilter(FILTERS_DEF[3]).setParameterList("status", status);

        return entityManager.createQuery(QUERY, Atividade.class).getResultList();

    }


    public List<AtividadeSimple> findAllSimple() {
        return atividadeRepository.findAllAtividadeSimple();
    }

    public Atividade findOne(PkAtividade chaveAtividade) {
        return atividadeRepository.getReferenceById(chaveAtividade);
    }

    public void delete(PkAtividade chaveAtividade) {
        atividadeRepository.deleteById(chaveAtividade);
    }

    @Override
    public ByteArrayOutputStream modeloCSV(String[] header) {
        return ImportacaoLote.super.modeloCSV(header);
    }

    @SuppressWarnings("DuplicatedCode")
    @Transactional
    @Override
    public void uploadLote(InputStream file) throws IOException, CsvValidationException, PersistenceException {

        InputStreamReader isr = new InputStreamReader(file);
        CSVReader reader = new CSVReaderBuilder(isr).withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .withSkipLines(1).build();
        String[] nextLine;

        while ((nextLine = reader.readNext()) != null) {

            Atividade atividade = new Atividade();

            String cnpjCliente = nextLine[0];
            String numEscopo = nextLine[1];
            String numContratoOrig = nextLine[2];
            String tipoServico = nextLine[3];
            String anoBase = nextLine[4];
            String horasVendidas = nextLine[5];
            String statusAtividade = nextLine[6];

            atividade.setPkAtividade(new PkAtividade(cnpjCliente, numEscopo, anoBase));
            atividade.setCliente(new Cliente(cnpjCliente));
            atividade.setEscopo(new Escopo(numEscopo));
            atividade.setNumContratoOrig(numContratoOrig);
            atividade.setTipoServico(TipoServico.valueOf(tipoServico));
            atividade.setAnoBase(anoBase);
            atividade.setHorasVendidas(Double.parseDouble(horasVendidas));
            atividade.setStatusAtividade(StatusAtividade.valueOf(statusAtividade));

            entityManager.merge(atividade);
        }

        isr.close();
        reader.close();

        entityManager.flush();

    }

}
