package br.cnac.analytics.api.email;

import br.cnac.analytics.service.model.colaborador.Colaborador;
import br.cnac.analytics.service.model.programacao.Programacao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;


@SuppressWarnings("SameParameterValue")
@RestController
public class EmailController {

    private static final String CUMPRIMENTOS = "</p><p>Atenciosamente,</p><p>Qualidade CNAC</p><p>[ESTE É UM E-MAIL AUTOMÁTICO DO WEB-SERVICE-CNAC]</p>";
    private static final String[] AREAS = {"Gerência", "Supervisão", "Auditoria"};


    /**
     * Função responsável por enviar uma requisição HTTPS ao Azure Logic App, para que este envie os emails no formato especificado.
     *
     * @param json Recebe um JSON com os dados a serem enviados para o Azure Logic App
     */
    public static void sendHttp(String json) throws IOException {
        try {

            String url = System.getenv("AZURE_LOGIC_APP");
            HttpURLConnection request = (HttpURLConnection) new URL(url).openConnection();

            // Define que a conexão pode enviar informações e obtê-las de volta:
            request.setDoOutput(true);
            request.setDoInput(true);

            // Define o content-type:
            request.setRequestProperty("Content-Type", "application/json");

            // Define o método da requisição:
            request.setRequestMethod("POST");

            // Conecta na URL:
            request.connect();

            // Escreve o objeto JSON usando o OutputStream da requisição:
            OutputStream outputStream = request.getOutputStream();
            outputStream.write(json.getBytes(StandardCharsets.UTF_8));

            request.getResponseMessage();

            request.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(e);
        }
    }

    /**
     * Função responsável por montar o JSON com as informações do e-mail.
     *
     * @param subject       - Assunto.
     * @param destinatarios - E-mails dos destinatários.
     * @param corpo         - Corpo de e-mail
     * @throws IOException - É lançado caso não seja possível gerar o JSON.
     */
    private static void sendEmail(String subject, String[] destinatarios, String corpo) throws IOException {

        Map<String, Object> map = new HashMap<>();

        //Formato de JSON definido no Azure Logic App.
        map.put("emails", destinatarios);
        map.put("task", subject);
        map.put("due", corpo);

        //Converte o JSON em String e chama a função de envio. 
        sendHttp(new ObjectMapper().writeValueAsString(map));

    }

    /**
     * Informa as áreas de CSA e TAAC sobre a exclusão de uma programação.
     *
     * @param p       - Programação em questão.
     * @param usuario - Usuário executor da ação.
     * @throws IOException É lançado quando não é possível enviar e-mail.
     */
    public static void sendEmailCancelamento(Programacao p, String usuario) throws IOException {

        String corpo = "<p>Prezados(as),</p>"
                + "<p>A programação da cooperativa " + p.getCli().getNumCoop() + "-" + p.getCli().getSiglaCoop()
                + ", com início em: " + convertDateToString(p.getDtInicio()) + " foi deletada pelo usuário " + usuario
                + ". Esta possuía amostra gerada, requer atenção!</p>";

        sendEmail("Programação Deletada!", getEmails(p, true), corpo + CUMPRIMENTOS);

    }

    /**
     * @param progAnterior - Programação Anterior
     * @param progAtual    - Programação Atual
     * @param usuario      - Usuário executor da ação.
     * @throws IOException É lançado quando não é possível enviar e-mail.
     */
    public static boolean sendEmailAlteracaoColab(Programacao progAnterior, Programacao progAtual, String usuario)
            throws IOException {

        List<String> atual = progAtual.getColaboradores().stream().map(Colaborador::getCpfCnpj)
                .toList();
        List<String> anterior = progAnterior.getColaboradores().stream().map(Colaborador::getCpfCnpj)
                .toList();

        if (!atual.equals(anterior)) {

            Set<String> removido = anterior.stream()
                    .filter(c -> !atual.contains(c)).collect(Collectors.toSet());

            if (!removido.isEmpty()) {

                Set<Colaborador> col = progAnterior.getColaboradores().stream()
                        .filter(c -> removido.contains(c.getCpfCnpj()))
                        .collect(Collectors.toSet());
                sendEmailColabRemovido(col, progAnterior, usuario);
            }

            return true;
        }

        return false;
    }

    /**
     * Informa aos atores da programação sobre a prorrogação da mesma.
     *
     * @param progAnterior - Programação Anterior
     * @param progAtual    - Programação Atual
     * @param usuario      - Usuário executor da ação.
     * @throws IOException É lançado quando não é possível enviar e-mail.
     */
    public static void sendEmailProrrogacaoProg(Programacao progAnterior, Programacao progAtual, String usuario)
            throws IOException {

        String corpo = ("<p>Prezados(as),</p><p>A programação da cooperativa "
                + progAnterior.getCli().getNumCoop()
                + "-" + progAnterior.getCli().getSiglaCoop()
                + " foi prorrogada pelo usuário: " + usuario + ", para o dia : "
                + convertDateToString(progAtual.getDtInicio()))
                + ". A data anterior era: " + convertDateToString(progAnterior.getDtInicio());

        sendEmail("Programação prorrogada!", getEmails(progAtual, true), corpo + CUMPRIMENTOS);

    }

    /**
     * Informa aos atores da programação sobre o adiantamento da mesma.
     *
     * @param progAnterior - Programação Anterior
     * @param progAtual    - Programação Atual
     * @param usuario      - Usuário executor da ação.
     * @throws IOException É lançado quando não é possível enviar e-mail.
     */
    public static void sendEmailAdiantamentoProg(Programacao progAnterior, Programacao progAtual, String usuario)
            throws IOException {

        String corpo = ("<p>Prezados(as),</p><p>A programação da cooperativa "
                + progAnterior.getCli().getNumCoop()
                + "-" + progAnterior.getCli().getSiglaCoop()
                + " foi adiantada pelo usuário: " + usuario + ", para o dia : "
                + convertDateToString(progAtual.getDtInicio()))
                + ". A data anterior era: " + convertDateToString(progAnterior.getDtInicio());

        sendEmail("Programação adiantada!", getEmails(progAtual, true), corpo + CUMPRIMENTOS);

    }

    /**
     * Informa que os colaboradores passados como parâmetro foram removidos.
     *
     * @param c       - Colaborador removido da programação
     * @param p       - Programação em questão.
     * @param usuario - Usuário que executou a ação.
     * @throws IOException É lançado quando não é possível enviar e-mail.
     */
    private static void sendEmailColabRemovido(Set<Colaborador> c, Programacao p, String usuario) throws IOException {

        String corpo = ("<p>Prezados(as),</p><p>Você foi removido da programação da cooperativa "
                + p.getCli().getNumCoop()
                + "-" + p.getCli().getSiglaCoop()
                + " pelo usuário  " + usuario + ", que iniciaria em : "
                + convertDateToString(p.getDtInicio()));

        if (!c.isEmpty())
            sendEmail("Você foi removido de uma programação!",
                    c.stream().map(Colaborador::getEmail).toArray(String[]::new),
                    corpo + CUMPRIMENTOS);

    }

    /**
     * @param p - Programação em questão
     * @throws IOException É lançado quando não é possível enviar e-mail.
     */
    public static void sendEmailDispAmostra(Programacao p) throws IOException {

        sendEmailAlocacaoEquipe(p);

    }

    /**
     * Envia e-mail informando da alocação de trabalhos.
     *
     * @param p - Programação em questão.
     * @throws IOException É lançado quando não é possível enviar e-mail.
     */
    public static void sendEmailAlocacaoEquipe(Programacao p) throws IOException {

        HashMap<String, List<Colaborador>> map = getColabClassificadosByArea(p);

        String corpo = ("<p>Prezados(as),</p><p>Foi gerada documentação de solicitação inicial e amostras para cooperativa:  "
                + p.getCli().getNumCoop()
                + "-" + p.getCli().getSiglaCoop() + ", central: " + p.getCli().getCodCentral() + " com a data base em: " + p.getDtBase() +
                "</p><p>Início em : " + convertDateToString(p.getDtInicio()) + " Fim em: " + convertDateToString(p.getDtFim()) + "</p>" +
                "<p>Tipo de serviço: " + p.getTipoServico() + "</p>\n" +
                "<p>Escopos aplicáveis: " + p.getStringEscopos() + "</p>\n" +
                "<p>Equipe: </p>" +
                "<p>Gerente(s): </p>" + "<p>" + map.get(AREAS[0]) + "</p>" +
                "<p>Supervisor(es): </p>" + "<p>" + map.get(AREAS[1]) + "</p>" +
                "<p>Auditor(es): </p>" + "<p>" + map.get(AREAS[2]) + "</p>");

        sendEmail("Solicitação inicial disponível - " + p.getCli().getNumCoop()
                        + "-" + p.getCli().getSiglaCoop(),
                getEmails(p, true), corpo + CUMPRIMENTOS);

    }

    /**
     * @param p   - Programação em questão
     * @param csa - true caso se deseje colocar o CSA em cópia.
     * @return Retorna um array com os destinatários.
     */
    private static String[] getEmails(Programacao p, boolean csa) {

        HashMap<String, List<Colaborador>> map = getColabClassificadosByArea(p);

        Set<Colaborador> destinatarios = new HashSet<>();
        destinatarios.addAll(map.get(AREAS[0]));
        destinatarios.addAll(map.get(AREAS[1]));
        destinatarios.addAll(map.get(AREAS[2]));

        List<String> emails = destinatarios.stream().map(Colaborador::getEmail).collect(Collectors.toList());

        if (csa)
            emails.add("csa@cnac.coop.br");

        return emails.toArray(new String[0]);
    }

    private static HashMap<String, List<Colaborador>> getColabClassificadosByArea(Programacao p) {

        List<Colaborador> gerentes = p.getGestores().stream().filter(c -> c.getCargo().getArea().equals(AREAS[0]))
                .toList();
        List<Colaborador> supervisores = p.getGestores().stream().filter(c -> c.getCargo().getArea().equals(AREAS[1]))
                .toList();
        List<Colaborador> auditores = new ArrayList<>(p.getColaboradores());

        HashMap<String, List<Colaborador>> map = new HashMap<>();

        map.put(AREAS[0], gerentes);
        map.put(AREAS[1], supervisores);
        map.put(AREAS[2], auditores);

        return map;
    }

    protected static String convertDateToString(java.sql.Date date) {
        return DateFormatUtils.format(date, "dd/MM/yyyy");
    }
}
