package br.cnac.analytics.domain.dao.cliente;

import br.cnac.analytics.api.atividade.AtividadeController;
import br.cnac.analytics.service.interfaces.cliente.ClientSimple;
import br.cnac.analytics.service.interfaces.cliente.ClienteRepository;
import br.cnac.analytics.service.interfaces.cliente.ClienteSemProgramacaoRepository;
import br.cnac.analytics.service.interfaces.importacao_lote.ImportacaoLote;
import br.cnac.analytics.service.interfaces.relatorio.ControleCSA;
import br.cnac.analytics.service.model.cliente.Cliente;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings({"DuplicatedCode", "SpringJavaInjectionPointsAutowiringInspection"})
@Repository
public class ClienteDAO implements ImportacaoLote {

    /**
     * Definição dos filtros da entidade
     */
    private static final String ANO_BASE = "anoBase";
    private static final String ESCRITORIO = "offices";
    private static final String TIPO_SERVICO = "tipoServico";
    private static final String[] FILTERS = {"anoBaseClientFilter", "officeClientFilter", "tipoServicoClientFilter"};

    private final ClienteRepository clienteRepository;

    private final EntityManager entityManager;

    public ClienteDAO(ClienteRepository clienteRepository, EntityManager entityManager) {
        this.clienteRepository = clienteRepository;
        this.entityManager = entityManager;
    }

    public void save(Cliente cliente) {
        clienteRepository.save(cliente);
    }

    public List<ControleCSA> getControleCSA(String anoBase) {
        return clienteRepository.obtemRelatorio(anoBase);
    }

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Set<Cliente> findAllByFilters(String[] anosBases, String[] escritorios, String[] tipoServicos,
                                         String[] gestores) {

        if (anosBases == null)
            anosBases = AtividadeController.getBiggerYear().toArray(new String[0]);

        Session session = entityManager.unwrap(Session.class);

        session.enableFilter(FILTERS[0]).setParameterList(ANO_BASE, anosBases);

        if (escritorios != null)
            session.enableFilter(FILTERS[1]).setParameterList(ESCRITORIO, escritorios);

        if (tipoServicos != null)
            session.enableFilter(FILTERS[2]).setParameterList(TIPO_SERVICO, tipoServicos);

        Set<Cliente> clientes = clienteRepository
                .findAll()
                .stream()
                .filter(c -> !c.getProgramacoes().isEmpty())
                .collect(Collectors.toSet());

        if (gestores != null) {
            clientes.forEach(c -> c.filtraGestores(gestores));
        }

        return clientes;
    }

    public Set<ClientSimple> findSimple() {
        return clienteRepository.findAllClientSimple();
    }

    public Cliente findOne(String cnpj, String anoBase) {

        Session session = entityManager.unwrap(Session.class);
        session.enableFilter(FILTERS[0]).setParameterList(ANO_BASE, Collections.singleton(anoBase));

        return clienteRepository.getReferenceById(cnpj);
    }

    public List<Cliente> findAllByAnoBase(String anoBase) {

        Session session = entityManager.unwrap(Session.class);
        session.enableFilter(FILTERS[0]).setParameterList(ANO_BASE, Collections.singleton(anoBase));

        return clienteRepository.findAll();
    }

    public void delete(String cnpj) {
        clienteRepository.deleteById(cnpj);
    }

    public Set<ClienteSemProgramacaoRepository> findCoopSemProgramacao(String anoBase) {
        return clienteRepository.findClientesSemProgramacao().stream()
                .filter(c -> c.getAnoBase().equals(anoBase)).collect(Collectors.toSet());
    }

    @Override
    public ByteArrayOutputStream modeloCSV(String[] header) {
        return ImportacaoLote.super.modeloCSV(header);
    }

    @Transactional
    @Override
    public void uploadLote(InputStream file) throws IOException, CsvValidationException, PersistenceException {

        InputStreamReader isr = new InputStreamReader(file);
        CSVReader reader = new CSVReaderBuilder(isr).withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .withSkipLines(1).build();
        String[] nextLine;

        while ((nextLine = reader.readNext()) != null) {

            Cliente c = new Cliente();

            String cnpjCliente = nextLine[0];
            String numCoop = nextLine[1];
            String siglaCoop = nextLine[2];
            String razaoSocial = nextLine[3];
            String codCentral = nextLine[4];
            String siglaCentral = nextLine[5];
            String codSistema = nextLine[6];
            String sisbr = nextLine[7];
            String segmentacao = nextLine[8];
            String classe = nextLine[9];
            String municipio = nextLine[10];
            String estado = nextLine[11];

            c.setCnpjCliente(cnpjCliente);
            c.setNumCoop(numCoop);
            c.setSiglaCoop(siglaCoop);
            c.setRazaoSocial(razaoSocial);
            c.setCodCentral(codCentral);
            c.setSiglaCentral(siglaCentral);
            c.setCodSistema(codSistema);
            c.setSisbr(Boolean.parseBoolean(sisbr));
            c.setSegmentacao(segmentacao);
            c.setClasse(classe);
            c.setMunicipio(municipio);
            c.setEstado(estado);

            entityManager.merge(c);
        }

        isr.close();
        reader.close();

        entityManager.flush();

    }

}
