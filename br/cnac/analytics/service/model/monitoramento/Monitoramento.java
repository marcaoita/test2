package br.cnac.analytics.service.model.monitoramento;

import br.cnac.analytics.service.model.atividade.entity.Atividade;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@SuppressWarnings("JpaDataSourceORMInspection")
@Entity
@Table(name = "monitoramentos")
public class Monitoramento implements Serializable {

    @Serial
    private static final long serialVersionUID = 1905122041950251207L;

    @Id
    @NotNull
    private String numAudit;
    private String status;
    private Date dtEftAudit;
    private double emRevisao;
    private double executado;
    private double revisado;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "atividade_fk_cnpj_cliente", referencedColumnName = "fk_cnpj_cliente")
    @JoinColumn(name = "atividade_fk_num_escopo", referencedColumnName = "fk_num_escopo")
    @JoinColumn(name = "atividade_ano_base", referencedColumnName = "anoBase")
    @JsonIgnoreProperties("monitoramento")
    private Atividade atividade;

    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }

    public String getNumAudit() {
        return numAudit;
    }

    public void setNumAudit(String numAudit) {
        this.numAudit = numAudit;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDtEftAudit() {
        return dtEftAudit;
    }

    public void setDtEftAudit(Date dtEftAudit) {
        this.dtEftAudit = dtEftAudit;
    }

    public double getEmRevisao() {
        return emRevisao;
    }

    public void setEmRevisao(double emRevisao) {
        this.emRevisao = emRevisao;
    }

    public double getExecutado() {
        return executado;
    }

    public void setExecutado(double executado) {
        this.executado = executado;
    }

    public double getRevisado() {
        return revisado;
    }

    public void setRevisado(double revisado) {
        this.revisado = revisado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Monitoramento that = (Monitoramento) o;
        return numAudit.equals(that.numAudit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numAudit);
    }
}
