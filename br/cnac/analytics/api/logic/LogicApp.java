package br.cnac.analytics.api.logic;

import br.cnac.analytics.domain.dao.monitoramento.MonitoramentoDAO;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.PersistenceException;
import java.io.IOException;

@RestController
@RequestMapping("logic-app/")
public class LogicApp {

    private final MonitoramentoDAO monitoramentoDAO;


    public LogicApp(MonitoramentoDAO monitoramentoDAO) {
        this.monitoramentoDAO = monitoramentoDAO;
    }

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void upload(@RequestParam("files") MultipartFile[] files) throws IOException {

        for (MultipartFile file : files) {

            try {

                monitoramentoDAO.uploadLote(file.getInputStream());

            } catch (IOException | PersistenceException e) {
                throw new IOException(e);
            } catch (CsvValidationException e) {
                e.printStackTrace();
            }
        }
    }

}
