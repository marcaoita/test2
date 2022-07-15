package br.cnac.analytics.domain.dto.monitoramento;

import br.cnac.analytics.service.model.monitoramento.Monitoramento;

import java.sql.Date;

public class MonitoramentoDTO {

    private String numAudit;
    private String status;
    private Date dtEftAudit;
    private double emRevisao;
    private double executado;
    private double revisado;

    public String getNumAudit() {
        return numAudit;
    }

    @SuppressWarnings("unused")
    public void setNumAudit(String numAudit) {
        this.numAudit = numAudit;
    }

    public String getStatus() {
        return status;
    }

    @SuppressWarnings("unused")
    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDtEftAudit() {
        return dtEftAudit;
    }

    @SuppressWarnings("unused")
    public void setDtEftAudit(Date dtEftAudit) {
        this.dtEftAudit = dtEftAudit;
    }

    public double getEmRevisao() {
        return emRevisao;
    }

    @SuppressWarnings("unused")
    public void setEmRevisao(double emRevisao) {
        this.emRevisao = emRevisao;
    }

    public double getExecutado() {
        return executado;
    }

    @SuppressWarnings("unused")
    public void setExecutado(double executado) {
        this.executado = executado;
    }

    public double getRevisado() {
        return revisado;
    }

    @SuppressWarnings("unused")
    public void setRevisado(double revisado) {
        this.revisado = revisado;
    }

    public Monitoramento convertDTOToEntity() {

        Monitoramento m = new Monitoramento();

        m.setNumAudit(this.getNumAudit());
        m.setStatus(this.getStatus());
        m.setExecutado(this.getExecutado());
        m.setEmRevisao(this.getEmRevisao());
        m.setRevisado(this.getRevisado());
        m.setDtEftAudit(this.getDtEftAudit());

        return m;
    }
}
