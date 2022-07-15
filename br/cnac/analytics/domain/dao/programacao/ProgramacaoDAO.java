package br.cnac.analytics.domain.dao.programacao;

import br.cnac.analytics.service.enumeration.TipoServico;
import br.cnac.analytics.service.interfaces.importacao_lote.ImportacaoLote;
import br.cnac.analytics.service.interfaces.programacao.ProgramacaoRepository;
import br.cnac.analytics.service.model.cliente.Cliente;
import br.cnac.analytics.service.model.colaborador.Colaborador;
import br.cnac.analytics.service.model.programacao.Programacao;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class ProgramacaoDAO implements ImportacaoLote {

    private final ProgramacaoRepository programacaoRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public ProgramacaoDAO(ProgramacaoRepository programacaoRepository) {
        this.programacaoRepository = programacaoRepository;
    }

    public Programacao save(Programacao programacao) {
        return programacaoRepository.save(programacao);
    }

    public Programacao findOne(Long id) {

        return programacaoRepository.getReferenceById(id);
    }

    public void delete(Long id) {
        programacaoRepository.deleteById(id);
    }

    @SuppressWarnings("unused")
    public List<Programacao> findAll() {
        return programacaoRepository.findAll();
    }

    @Override
    public ByteArrayOutputStream modeloCSV(String[] header, List<String[]> exemplo) {
        return ImportacaoLote.super.modeloCSV(header, exemplo);
    }

    @Transactional
    @Override
    public void uploadLote(InputStream file) throws IOException, CsvValidationException, PersistenceException {

        InputStreamReader isr = new InputStreamReader(file);
        CSVReader reader = new CSVReaderBuilder(isr).withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .withSkipLines(1).build();

        String[] nextLine;

        while ((nextLine = reader.readNext()) != null) {

            Programacao p = new Programacao();

            String cnpjCliente = "09140486";
            String colaboradorCPF = nextLine[0];
            String gestorCPF = nextLine[1];
            String dtInicio = nextLine[2];
            String dtFim = nextLine[3];
            String tipoVisita = "Home Office";
            String tipoServico = nextLine[4];
            String anoBase = nextLine[5];
            String escritorioOrig = nextLine[6];

            Set<Colaborador> c = new HashSet<>();
            c.add(new Colaborador(colaboradorCPF));

            Set<Colaborador> g = new HashSet<>();
            g.add(new Colaborador(gestorCPF));

            p.setCli(new Cliente(cnpjCliente));
            p.setAtividades(null);
            p.setColaboradores(c);
            p.setGestores(g);
            p.setDtInicio(convertData(dtInicio));
            p.setDtFim(convertData(dtFim));
            p.setDtInicioExe(convertData(dtInicio));
            p.setDtFimExe(convertData(dtFim));
            p.setTipoVisita(tipoVisita);
            p.setTipoServico(TipoServico.valueOf(tipoServico));
            p.setAnoBase(anoBase);
            p.setEscritorioOrig(escritorioOrig);

            entityManager.merge(p);
        }

        isr.close();
        reader.close();

        entityManager.flush();


    }

    private Date convertData(String data) {

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return new Date(formato.parse(data).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
