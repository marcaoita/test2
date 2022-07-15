package br.cnac.analytics.service.interfaces.importacao_lote;

import com.opencsv.exceptions.CsvValidationException;

import javax.persistence.PersistenceException;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface ImportacaoLote {

    @SuppressWarnings("unused")
    void uploadLote(InputStream file) throws IOException, CsvValidationException, PersistenceException;

    default ByteArrayOutputStream modeloCSV(String[] cabecalho) {

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            List<String[]> dataLines = new ArrayList<>();

            dataLines.add(cabecalho);

            StringBuilder sb = new StringBuilder();

            for (String[] s : dataLines)
                sb.append(convertToCSV(s));

            new DataOutputStream(byteArrayOutputStream).write(String.valueOf(sb).getBytes());
            byteArrayOutputStream.flush();

            return byteArrayOutputStream;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    default ByteArrayOutputStream modeloCSV(String[] cabecalho, List<String[]> dadosExemplo) {

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            List<String[]> dataLines = new ArrayList<>();

            dataLines.add(cabecalho);

            dataLines.addAll(dadosExemplo);

            StringBuilder sb = new StringBuilder();

            for (String[] s : dataLines)
                sb.append(convertToCSV(s));

            new DataOutputStream(byteArrayOutputStream).write(String.valueOf(sb).getBytes());
            byteArrayOutputStream.flush();

            return byteArrayOutputStream;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(";")) + "\n";
    }

    private String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

}
