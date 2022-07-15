package br.cnac.analytics.domain.dto.parecer;

import java.sql.Date;

import br.cnac.analytics.service.enumeration.Opiniao;
import br.cnac.analytics.service.model.parecer.Parecer;
import br.cnac.analytics.service.model.programacao.Programacao;

public class ParecerDTO {

    private Long id;

    private Date dtParecer;
    private Date dtEnvio;
    private Date dtPublicacao;
    private Date dtEmissao;
    private Opiniao opiniao;
    private String paraModificacao;
    private boolean enfase;
    private String paraEnfase;
    private boolean outrosAssuntos;
    private String paraOutrosAssuntos;
    private long idProgramacao;

    @SuppressWarnings("unused")
    public Date getDtParecer() {
        return dtParecer;
    }

    @SuppressWarnings("unused")
    public void setDtParecer(Date dtParecer) {
        this.dtParecer = dtParecer;
    }

    /**
     * @return the idProgramacao
     */
    @SuppressWarnings("unused")
    public long getIdProgramacao() {
        return idProgramacao;
    }

    /**
     * @param idProgramacao the idProgramacao to set
     */
    @SuppressWarnings("unused")
    public void setIdProgramacao(long idProgramacao) {
        this.idProgramacao = idProgramacao;
    }

    /**
     * @return the id
     */
    @SuppressWarnings("unused")
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    @SuppressWarnings("unused")
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the dtEnvio
     */
    @SuppressWarnings("unused")
    public Date getDtEnvio() {
        return dtEnvio;
    }

    /**
     * @param dtEnvio the dtEnvio to set
     */
    @SuppressWarnings("unused")
    public void setDtEnvio(Date dtEnvio) {
        this.dtEnvio = dtEnvio;
    }

    /**
     * @return the dtPublicacao
     */
    @SuppressWarnings("unused")
    public Date getDtPublicacao() {
        return dtPublicacao;
    }

    /**
     * @param dtPublicacao the dtPublicacao to set
     */
    @SuppressWarnings("unused")
    public void setDtPublicacao(Date dtPublicacao) {
        this.dtPublicacao = dtPublicacao;
    }

    /**
     * @return the dtEmissao
     */
    @SuppressWarnings("unused")
    public Date getDtEmissao() {
        return dtEmissao;
    }

    /**
     * @param dtEmissao the dtEmissao to set
     */
    @SuppressWarnings("unused")
    public void setDtEmissao(Date dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    /**
     * @return the opiniao
     */
    @SuppressWarnings("unused")
    public Opiniao getOpiniao() {
        return opiniao;
    }

    /**
     * @param opiniao the opiniao to set
     */
    @SuppressWarnings("unused")
    public void setOpiniao(Opiniao opiniao) {
        this.opiniao = opiniao;
    }

    /**
     * @return the paraModificacao
     */
    @SuppressWarnings("unused")
    public String getParaModificacao() {
        return paraModificacao;
    }

    /**
     * @param paraModificacao the paraModificacao to set
     */
    @SuppressWarnings("unused")
    public void setParaModificacao(String paraModificacao) {
        this.paraModificacao = paraModificacao;
    }

    /**
     * @return the enfase
     */
    @SuppressWarnings("unused")
    public boolean isEnfase() {
        return enfase;
    }

    /**
     * @param enfase the enfase to set
     */
    @SuppressWarnings("unused")
    public void setEnfase(boolean enfase) {
        this.enfase = enfase;
    }

    /**
     * @return the paraEnfase
     */
    @SuppressWarnings("unused")
    public String getParaEnfase() {
        return paraEnfase;
    }

    /**
     * @param paraEnfase the paraEnfase to set
     */
    @SuppressWarnings("unused")
    public void setParaEnfase(String paraEnfase) {
        this.paraEnfase = paraEnfase;
    }

    /**
     * @return the outrosAssuntos
     */
    @SuppressWarnings("unused")
    public boolean isOutrosAssuntos() {
        return outrosAssuntos;
    }

    /**
     * @param outrosAssuntos the outrosAssuntos to set
     */
    @SuppressWarnings("unused")
    public void setOutrosAssuntos(boolean outrosAssuntos) {
        this.outrosAssuntos = outrosAssuntos;
    }

    /**
     * @return the paraOutrosAssuntos
     */
    @SuppressWarnings("unused")
    public String getParaOutrosAssuntos() {
        return paraOutrosAssuntos;
    }

    /**
     * @param paraOutrosAssuntos the paraOutrosAssuntos to set
     */
    @SuppressWarnings("unused")
    public void setParaOutrosAssuntos(String paraOutrosAssuntos) {
        this.paraOutrosAssuntos = paraOutrosAssuntos;
    }

    /**
     * @return Retorna um Objeto do tipo <code>Parecer<code/>
     */
    public Parecer convertDTOToEntity() {

        Parecer p = new Parecer();

        p.setDtParecer(this.dtParecer);
        p.setDtEmissao(this.dtEmissao);
        p.setDtEnvio(this.dtEnvio);
        p.setDtPublicacao(this.dtPublicacao);
        p.setEnfase(this.enfase);
        p.setId(this.id);
        p.setOpiniao(this.opiniao);
        p.setOutrosAssuntos(this.outrosAssuntos);
        p.setParaEnfase(this.paraEnfase);
        p.setParaModificacao(this.paraModificacao);
        p.setParaOutrosAssuntos(this.paraOutrosAssuntos);
        p.setProgramacao(new Programacao(this.idProgramacao));

        return p;

    }

}
