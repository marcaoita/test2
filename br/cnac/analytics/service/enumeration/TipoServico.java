package br.cnac.analytics.service.enumeration;

import java.util.Arrays;
import java.util.List;

public enum TipoServico {

    AC, AE, ES, TI, CSA, TREINAMENTO, FERIAS, LICENCA, COMPENSACAO, @SuppressWarnings("unused") ACOMPANHAMENTO, @SuppressWarnings("unused") RESERVA;

    /**
     * @return Retorna uma lista com os tipos de serviços operacionais.
     * São serviços operacionais AC, AE e ES.
     */
    public static List<TipoServico> getServicosOperacionais() {
        return Arrays.asList(TipoServico.AC, TipoServico.AE, TipoServico.ES, TipoServico.TI);
    }

    /**
     * @return Retorna uma lista com os tipos de serviços que indicam a ausência do executor, como férias e licenças.
     */
    public static List<TipoServico> getServicosAusencias() {
        return Arrays.asList(TipoServico.FERIAS, TipoServico.LICENCA);
    }


}
