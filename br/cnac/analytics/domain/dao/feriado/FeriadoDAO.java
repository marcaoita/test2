package br.cnac.analytics.domain.dao.feriado;

import br.cnac.analytics.service.enumeration.TipoFeriado;
import br.cnac.analytics.service.interfaces.feriado.FeriadoRepository;
import br.cnac.analytics.service.model.feriado.entity.Feriado;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Component
@Repository
public class FeriadoDAO {

    private final FeriadoRepository recessosRepository;

    private final EntityManager entityManager;

    public FeriadoDAO(FeriadoRepository recessosRepository, EntityManager entityManager) {
        this.recessosRepository = recessosRepository;
        this.entityManager = entityManager;
    }

    public void save(Feriado feriados) {
        recessosRepository.save(feriados);
    }

    @SuppressWarnings("unused")
    public Feriado findOne(String id) {
        return recessosRepository.getReferenceById(id);
    }

    public void delete(String id) {
        recessosRepository.deleteById(id);
    }

    @SuppressWarnings("unused")
    public List<Feriado> findAll() {

        return recessosRepository.findAll();
    }

    @Transactional
    public void uploadLote(InputStream file) throws IOException, ParseException, CsvValidationException {

        InputStreamReader isr = new InputStreamReader(file);
        CSVReader reader = new CSVReader(isr);

        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

        String[] nextLine;

        while ((nextLine = reader.readNext()) != null) {

            Feriado feriado = new Feriado();

            java.sql.Date data = new java.sql.Date(formato.parse(nextLine[0]).getTime());

            feriado.setDtRecesso(data);
            feriado.setNome(nextLine[1]);
            feriado.setTipoFeriado(TipoFeriado.valueOf(nextLine[2]));
            feriado.setUf(nextLine[3]);
            feriado.setMunicipio(nextLine[4]);

            entityManager.persist(feriado);
        }

        isr.close();
        reader.close();

        entityManager.flush();

    }

}
