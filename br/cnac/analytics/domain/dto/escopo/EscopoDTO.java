package br.cnac.analytics.domain.dto.escopo;

import br.cnac.analytics.service.model.escopo.Escopo;

public class EscopoDTO {

    private String numEscopo;

    private String tipoEscopo;

    private String aplicacao;

    private String escopoAgregador;

    private String apelido;

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    /**
     * @return the escopoAgregador
     */
    public String getEscopoAgregador() {
        return escopoAgregador;
    }

    /**
     * @param escopoAgregador the escopoAgregador to set
     */
    @SuppressWarnings("unused")
    public void setEscopoAgregador(String escopoAgregador) {
        this.escopoAgregador = escopoAgregador;
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

        if (numEscopo.length() < 3)
            throw new IllegalArgumentException("Número de escopo deve ter ao menos 3 dígitos!");

        this.numEscopo = numEscopo;
    }

    /**
     * @return the tipoEscopo
     */
    public String getTipoEscopo() {
        return tipoEscopo;
    }

    /**
     * @param tipoEscopo the tipoEscopo to set
     */
    @SuppressWarnings("unused")
    public void setTipoEscopo(String tipoEscopo) {
        this.tipoEscopo = tipoEscopo;
    }

    /**
     * @return the aplicacao
     */
    public String getAplicacao() {
        return aplicacao;
    }

    /**
     * @param aplicacao the aplicacao to set
     */
    @SuppressWarnings("unused")
    public void setAplicacao(String aplicacao) {
        this.aplicacao = aplicacao;
    }

    /**
     * @return Retorna um objeto do tipo <code>Escopo</code>
     */
    public Escopo convertDTOToEntity() {

        Escopo escopo = new Escopo();
        escopo.setNumEscopo(this.getNumEscopo());
        escopo.setTipoEscopo(this.getTipoEscopo());
        escopo.setAplicacao(this.getAplicacao());
        escopo.setEscopoAgregador(this.getEscopoAgregador());
        escopo.setApelido(this.getApelido());
        return escopo;
    }

}