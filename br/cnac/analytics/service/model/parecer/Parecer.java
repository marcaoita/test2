package br.cnac.analytics.service.model.parecer;

import br.cnac.analytics.service.enumeration.Opiniao;
import br.cnac.analytics.service.model.programacao.Programacao;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;

@Entity
@Table(name = "pareceres")
public class Parecer implements Serializable {

    @Serial
    private static final long serialVersionUID = 1905122041950251207L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private Date dtParecer;
    private Date dtEnvio;
    private Date dtPublicacao;
    private Date dtEmissao;
    @Enumerated(EnumType.STRING)
    private Opiniao opiniao;
    @Column(columnDefinition = "TEXT")
    private String paraModificacao;
    private boolean enfase;
    @Column(columnDefinition = "TEXT")
    private String paraEnfase;
    private boolean outrosAssuntos;
    @Column(columnDefinition = "TEXT")
    private String paraOutrosAssuntos;

    @OneToOne()
    @JoinColumn(name = "fk_programacao")
    @JsonIgnoreProperties({"parecer"})
    private Programacao programacao;

    @SuppressWarnings("unused")
    public String getStringOpiniao() {

        if (this.opiniao == null)
            return null;

        return this.opiniao.toString();

    }

    @SuppressWarnings("unused")
    public Date getDtParecer() {
        return dtParecer;
    }

    public void setDtParecer(Date dtParecer) {
        this.dtParecer = dtParecer;
    }

    /**
     * @return the programacao
     */
    public Programacao getProgramacao() {
        return programacao;
    }

    /**
     * @param programacao the programacao to set
     */
    public void setProgramacao(Programacao programacao) {
        this.programacao = programacao;
    }

    @SuppressWarnings("unused")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public Date getDtEnvio() {
        return dtEnvio;
    }

    public void setDtEnvio(Date dtEnvio) {
        this.dtEnvio = dtEnvio;
    }

    @SuppressWarnings("unused")
    public Date getDtPublicacao() {
        return dtPublicacao;
    }

    public void setDtPublicacao(Date dtPublicacao) {
        this.dtPublicacao = dtPublicacao;
    }

    @SuppressWarnings("unused")
    public Date getDtEmissao() {
        return dtEmissao;
    }

    public void setDtEmissao(Date dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    @SuppressWarnings("unused")
    public Opiniao getOpiniao() {
        return opiniao;
    }

    public void setOpiniao(Opiniao opiniao) {
        this.opiniao = opiniao;
    }

    @SuppressWarnings("unused")
    public String getParaModificacao() {
        return paraModificacao;
    }

    public void setParaModificacao(String paraModificacao) {
        this.paraModificacao = paraModificacao;
    }

    @SuppressWarnings("unused")
    public boolean isEnfase() {
        return enfase;
    }

    public void setEnfase(boolean enfase) {
        this.enfase = enfase;
    }

    @SuppressWarnings("unused")
    public String getParaEnfase() {
        return paraEnfase;
    }

    public void setParaEnfase(String paraEnfase) {
        this.paraEnfase = paraEnfase;
    }

    @SuppressWarnings("unused")
    public boolean isOutrosAssuntos() {
        return outrosAssuntos;
    }

    public void setOutrosAssuntos(boolean outrosAssuntos) {
        this.outrosAssuntos = outrosAssuntos;
    }

    @SuppressWarnings("unused")
    public String getParaOutrosAssuntos() {
        return paraOutrosAssuntos;
    }

    public void setParaOutrosAssuntos(String paraOutrosAssuntos) {
        this.paraOutrosAssuntos = paraOutrosAssuntos;
    }
}
