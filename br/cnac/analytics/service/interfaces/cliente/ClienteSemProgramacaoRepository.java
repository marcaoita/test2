package br.cnac.analytics.service.interfaces.cliente;

public interface ClienteSemProgramacaoRepository {

    @SuppressWarnings("unused")
    String getCnpjCliente();

    @SuppressWarnings("unused")
    String getCodCentral();

    @SuppressWarnings("unused")
    String getNumCoop();

    @SuppressWarnings("unused")
    String getSiglaCoop();

    String getAnoBase();

    @SuppressWarnings("unused")
    double getHorasVendidas();

    @SuppressWarnings("unused")
    String getTipoServico();

    @SuppressWarnings("unused")
    String getNumEscopo();

    @SuppressWarnings("unused")
    String getTipoEscopo();

}
