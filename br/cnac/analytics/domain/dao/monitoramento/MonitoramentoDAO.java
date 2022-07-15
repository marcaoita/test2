package br.cnac.analytics.domain.dao.monitoramento;

import br.cnac.analytics.api.cliente.ClienteController;
import br.cnac.analytics.domain.dto.filter.FilterDTO;
import br.cnac.analytics.service.interfaces.importacao_lote.ImportacaoLote;
import br.cnac.analytics.service.interfaces.monitoramento.MonitoramentoRel;
import br.cnac.analytics.service.interfaces.monitoramento.MonitoramentoRepository;
import br.cnac.analytics.service.model.atividade.chave_composta.PkAtividade;
import br.cnac.analytics.service.model.atividade.entity.Atividade;
import br.cnac.analytics.service.model.monitoramento.Monitoramento;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Component
@Repository
public class MonitoramentoDAO implements ImportacaoLote {

    private final MonitoramentoRepository monitoramentoRepository;
    private final EntityManager entityManager;

    public MonitoramentoDAO(MonitoramentoRepository monitoramentoRepository, EntityManager entityManager) {
        this.monitoramentoRepository = monitoramentoRepository;
        this.entityManager = entityManager;
    }

    public List<MonitoramentoRel> getRelatorio(FilterDTO filters) {

        List<MonitoramentoRel> m = monitoramentoRepository.obtemRelatorio(filters.getAnoBase());

        if (filters.getEscritorioColaborador() != null)
            m.removeIf(mo -> !Arrays.stream(filters.getEscritorioColaborador()).toList().contains(mo.getEscritorio()));

        if (filters.getTipoServico() != null) {
            if (!filters.getTipoServico()[0].equals("todos"))
                m.removeIf(mo -> !Arrays.stream(filters.getTipoServico()).toList().contains(mo.getTipoServico()));
        }

        if (filters.getMes() != null)
            m.removeIf(mo -> !Arrays.asList(filters.getMes()).contains(mo.getDtInicio().toLocalDate().getMonthValue()));

        if (filters.getStatus() != null)
            m.removeIf(mo -> !Arrays.asList(filters.getStatus()).contains(mo.getStatusRac()));

        if (filters.getGestores() != null)
            m.removeIf(mo -> !Arrays.asList(filters.getGestores()).contains(mo.getCpf()));

        return m;
    }

    /**
     * @param date - Data no formato DD/MM/YYYY a ser convertida.
     * @return Retorna um objeto do tipo date.
     */
    private static Date stringToDate(String date) {

        DateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return new Date(formato.parse(date).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save(Monitoramento m) {
        monitoramentoRepository.save(m);
    }

    @SuppressWarnings("unused")
    public Monitoramento findOne(long id) {
        return monitoramentoRepository.getReferenceById(id);
    }

    public void delete(long id) {
        monitoramentoRepository.deleteById(id);
    }

    public List<Monitoramento> findAll() {
        return monitoramentoRepository.findAll();
    }

    @Override
    @Transactional
    public void uploadLote(InputStream file) throws IOException, CsvValidationException, PersistenceException {

        Workbook wb = new XSSFWorkbook(file);

        Sheet sheetMonitoramento = wb.getSheetAt(0);

        int cont = 0;

        for (Row row : sheetMonitoramento) {

            if (cont < 2) {
                cont++;
                continue;
            }


            Iterator<Cell> cellIterator = row.cellIterator();

            Monitoramento m = new Monitoramento();

            String escopo = null;
            String cooperativa = null;
            String numAudit = null;
            String dtEftAudit = null;

            while (cellIterator.hasNext()) {

                Cell cell = cellIterator.next();

                //As colunas 3, 5 e 6 são ignoradas.
                switch (cell.getColumnIndex()) {
                    case 0 -> numAudit = cell.getStringCellValue();
                    case 1 -> escopo = cell.getStringCellValue();
                    case 2 -> cooperativa = cell.getStringCellValue();
                    case 4 -> m.setStatus(cell.getStringCellValue());
                    case 6 -> dtEftAudit = cell.getStringCellValue();
                    case 8 -> m.setExecutado(cell.getNumericCellValue());
                    case 9 -> m.setEmRevisao(cell.getNumericCellValue());
                    case 10 -> m.setRevisado(cell.getNumericCellValue());
                }
            }

            String numEscopo = null;
            String anoBase = null;
            String numCoop = null;

            if (numAudit != null)
                anoBase = numAudit.split("/")[0];

            if (escopo != null)
                numEscopo = escopo.split(" ")[1];

            if (cooperativa != null)
                numCoop = cooperativa.split(" ")[0];

            if (!dtEftAudit.equals(""))
                m.setDtEftAudit(stringToDate(dtEftAudit));

            //Árvore binária de busca para retornar o cnpj do cliente através do número da coop.
            String cnpjCliente = ClienteController.getClienteByNumCoop(numCoop).getCnpjCliente();

            PkAtividade pk = new PkAtividade(cnpjCliente, numEscopo, anoBase);
            Atividade a = entityManager.getReference(Atividade.class, pk);

            m.setNumAudit(numAudit);
            m.setAtividade(a);
            a.setMonitoramento(m);

            entityManager.merge(a);
            entityManager.merge(m);

        }

        wb.close();

        entityManager.flush();
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
