package br.cnac.analytics.service.interfaces.monitoramento;

import br.cnac.analytics.api.configuracao.ConfiguracaoController;
import br.cnac.analytics.api.feriado.FeriadoController;

import java.sql.Date;
import java.util.Calendar;

public interface MonitoramentoRel {

    String getCnpjCliente();

    String getNumCoop();

    String getSiglaCoop();

    String getEscritorio();

    String getEscopo();

    String getHorasVendidas();

    Date getDtInicio();

    Date getDtFim();

    String getEstado();

    String getMunicipio();

    default Date getDtFimPrev() {
        return increDecreData(getDtFim(), ConfiguracaoController.getVigente(getDtFim()).getDiasRev(), new String[]{getMunicipio()}, new String[]{getEstado()}, true, true);
    }

    default Date getDtMaxEmiRac() {
        return increDecreData(getDtFimPrev(), ConfiguracaoController.getVigente(getDtFim()).getPrazoRac() + 1, new String[]{getMunicipio()}, new String[]{getEstado()}, true, false);
    }

    default String getStatusRac() {

        Date today = new java.sql.Date(new java.util.Date().getTime());

        if ((today.before(getDtMaxEmiRac()) && getEmissaoRac() == null))
            return "Em dia";

        if ((!today.before(getDtMaxEmiRac()) && getEmissaoRac() == null))
            return "Atrasado";

        if (getEmissaoRac().after(getDtMaxEmiRac()))
            return "Entregue com atraso";

        if (!getEmissaoRac().after(getDtMaxEmiRac()))
            return "Entregue";

        return null;
    }

    String getNota();

    String getAvaliacao();

    String getNumAudit();

    default String getAvaliacaoPreenchida() {

        if (getAvaliacao() == null)
            return "Não";

        if (getAvaliacao().isEmpty())
            return "Não";

        return "Sim";
    }

    Date getEmissaoRac();

    Date getEnvioRAC();

    String getCpf();

    String getExecutado();

    String getARevisar();

    String getRevisado();

    String getStatus();

    String getAnoBase();

    String getNumEscopo();

    String getTipoServico();

    String getGestores();

    String getCodCentral();


    /**
     * @param dtInicio   - data de início.
     * @param diasMaximo - dias úteis máximo desejado para incrementar ou
     *                   decrementar.
     * @param municipios - Município para fins de feriado.
     * @param estados    - Unidade da federação para fins de feriados.
     * @param incrementa - informa se o método deverá incrementar (true) ou
     *                   decrementar (false) a data.
     * @return Retorna uma data incrementada ou decrementada levando em consideração
     * feriados e dias úteis.
     */
    private Date increDecreData(Date dtInicio, double diasMaximo, String[] municipios, String[] estados,
                                boolean incrementa, boolean consideraDiaUtil) {

        Calendar cDate = Calendar.getInstance();
        Calendar cDateFinal = Calendar.getInstance();

        cDate.setTime(dtInicio);

        int incremento = 1;

        if (!incrementa)
            incremento = -1;

        double diasUteis = 0;

        while (diasUteis < diasMaximo) {

            cDateFinal.setTime(cDate.getTime());

            int diaDaSemana = cDate.get(Calendar.DAY_OF_WEEK);

            if (consideraDiaUtil) {
                if (diaDaSemana != Calendar.SATURDAY && diaDaSemana != Calendar.SUNDAY
                        && FeriadoController.findByDate(new Date(cDate.getTime().getTime()), estados,
                        municipios).isEmpty())
                    diasUteis++;
            } else {
                diasUteis++;
            }

            cDate.add(Calendar.DATE, incremento);

        }

        return new Date(cDateFinal.getTime().getTime());
    }
}
