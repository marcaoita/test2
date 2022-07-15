package br.cnac.analytics.domain.dto.atividade;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import br.cnac.analytics.service.enumeration.StatusAtividade;
import br.cnac.analytics.service.enumeration.TipoServico;
import br.cnac.analytics.service.model.atividade.chave_composta.PkAtividade;
import br.cnac.analytics.service.model.atividade.entity.Atividade;
import br.cnac.analytics.service.model.cliente.Cliente;
import br.cnac.analytics.service.model.escopo.Escopo;

public class AtividadeDTO {

    private String cnpjCliente;
    private String numEscopo;
    private String numContratoOrig;
    private double horasVendidas;
    private String anoBase;
    private String tipoServico;
    private StatusAtividade statusAtividade;
    private boolean ampliacaoEscopo;
    private String requisitanteAmpliacaoEscopo;
    private String dtRescisao;

    private String descricaoAmpliacaoEscopo;

    public String getDescricaoAmpliacaoEscopo() {
        return descricaoAmpliacaoEscopo;
    }

    @SuppressWarnings("unused")
    public void setDescricaoAmpliacaoEscopo(String descricaoAmpliacaoEscopo) {
        this.descricaoAmpliacaoEscopo = descricaoAmpliacaoEscopo;
    }

    /**
     * @return the dtRescisao
     */
    public String getDtRescisao() {
        return dtRescisao;
    }

    /**
     * @param dtRescisao the dtRescisao to set
     */
    @SuppressWarnings("unused")
    public void setDtRescisao(String dtRescisao) {
        this.dtRescisao = dtRescisao;
    }

    /**
     * @return the requisitanteAmpliacaoEscopo
     */
    public String getRequisitanteAmpliacaoEscopo() {
        return requisitanteAmpliacaoEscopo;
    }

    /**
     * @param requisitanteAmpliacaoEscopo the requisitanteAmpliacaoEscopo to set
     */
    @SuppressWarnings("unused")
    public void setRequisitanteAmpliacaoEscopo(String requisitanteAmpliacaoEscopo) {
        this.requisitanteAmpliacaoEscopo = requisitanteAmpliacaoEscopo;
    }

    /**
     * @return the ampliacaoEscopo
     */
    public boolean isAmpliacaoEscopo() {
        return ampliacaoEscopo;
    }

    /**
     * @param ampliacaoEscopo the ampliacaoEscopo to set
     */
    public void setAmpliacaoEscopo(boolean ampliacaoEscopo) {
        this.ampliacaoEscopo = ampliacaoEscopo;
    }

    /**
     * @return the cnpjCliente
     */
    public String getCnpjCliente() {
        return cnpjCliente;
    }

    /**
     * @param cnpjCliente the cnpjCliente to set
     */
    @SuppressWarnings("unused")
    public void setCnpjCliente(String cnpjCliente) {
        this.cnpjCliente = cnpjCliente;
    }

    /**
     * @return the numEscopo
     */
    public String getNumEscopo() {
        return numEscopo;
    }

    /**
     * @param numEscopo the numEscopo to set
     */
    @SuppressWarnings("unused")
    public void setNumEscopo(String numEscopo) {
        this.numEscopo = numEscopo;
    }

    /**
     * @return the numContratoOrig
     */
    public String getNumContratoOrig() {
        return numContratoOrig;
    }

    /**
     * @param numContratoOrig the numContratoOrig to set
     */
    @SuppressWarnings("unused")
    public void setNumContratoOrig(String numContratoOrig) {
        this.numContratoOrig = numContratoOrig;
    }

    /**
     * @return the horasVendidas
     */
    public double getHorasVendidas() {
        return horasVendidas;
    }

    /**
     * @param horasVendidas the horasVendidas to set
     */
    @SuppressWarnings("unused")
    public void setHorasVendidas(double horasVendidas) {
        this.horasVendidas = horasVendidas;
    }

    /**
     * @return the anoBase
     */
    public String getAnoBase() {
        return anoBase;
    }

    /**
     * @param anoBase the anoBase to set
     */
    @SuppressWarnings("unused")
    public void setAnoBase(String anoBase) {
        this.anoBase = anoBase;
    }

    /**
     * @return the tipoServico
     */
    public String getTipoServico() {
        return tipoServico;
    }

    /**
     * @param tipoServico the tipoServico to set
     */
    @SuppressWarnings("unused")
    public void setTipoServico(String tipoServico) {
        this.tipoServico = tipoServico;
    }

    /**
     * @return the statusAtividade
     */
    public StatusAtividade getStatusAtividade() {
        return statusAtividade;
    }

    /**
     * @param statusAtividade the statusAtividade to set
     */
    @SuppressWarnings("unused")
    public void setStatusAtividade(StatusAtividade statusAtividade) {
        this.statusAtividade = statusAtividade;
    }

    /**
     * @return Retorna um objeto do tipo <code>Atividade<code/>
     * @throws ParseException É lançado caso haja falha na conversão de datas.
     */
    public Atividade convertDTOToEntity() throws ParseException {

        Atividade atividade = new Atividade();

        atividade.setAnoBase(this.getAnoBase());
        atividade.setCliente(new Cliente(this.getCnpjCliente()));
        atividade.setEscopo(new Escopo(this.getNumEscopo()));
        atividade.setNumContratoOrig(this.getNumContratoOrig());
        atividade.setPkAtividade(
                new PkAtividade(this.getCnpjCliente(), this.getNumEscopo(), this.getAnoBase()));
        atividade.setStatusAtividade(this.getStatusAtividade());
        atividade.setTipoServico(TipoServico.valueOf(this.getTipoServico()));
        atividade.setAmpliacaoEscopo(this.isAmpliacaoEscopo());
        atividade.setHorasVendidas(this.getHorasVendidas());
        atividade.setRequisitanteAmpliacaoEscopo(this.getRequisitanteAmpliacaoEscopo());
        atividade.setDescricaoAmpliacaoEscopo(this.getDescricaoAmpliacaoEscopo());

        if (!"".equals(this.getDtRescisao())) {

            atividade.setDtRescisao(new java.sql.Date(stringToDate(this.getDtRescisao()).getTime()));

        }

        return atividade;
    }

    /**
     * @param date - Data no formato DD-MM-YYYY a ser convertida.
     * @return Retorna um objeto do tipo date.
     * @throws ParseException É lançada caso haja falha na conversão das datas.
     */
    private static Date stringToDate(String date) throws ParseException {

        DateFormat formato = new SimpleDateFormat("yyyy-MM-dd");

        return new Date(formato.parse(date).getTime());
    }

}
