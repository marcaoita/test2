package br.cnac.analytics.domain.dto.colaborador;

import br.cnac.analytics.service.model.cargo.Cargo;
import br.cnac.analytics.service.model.colaborador.Colaborador;

public class ColaboradorDTO {

    private String cpfCnpj;
    private String nome;
    private String cargoDesc;
    private String escritorioOrigem;
    private String municipio;
    private String estado;
    private String email;
    private String statusAtual;
    private String tipoAlocacao;

    /**
     * @return the cpfCnpj
     */
    public String getCpfCnpj() {
        return cpfCnpj;
    }

    /**
     * @param cpfCnpj the cpfCnpj to set
     */
    @SuppressWarnings("unused")
    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    @SuppressWarnings("unused")
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the cargoDesc
     */
    public String getCargoDesc() {
        return cargoDesc;
    }

    /**
     * @param cargoDesc the cargoDesc to set
     */
    @SuppressWarnings("unused")
    public void setCargoDesc(String cargoDesc) {
        this.cargoDesc = cargoDesc;
    }

    /**
     * @return the escritorioOrigem
     */
    public String getEscritorioOrigem() {
        return escritorioOrigem;
    }

    /**
     * @param escritorioOrigem the escritorioOrigem to set
     */
    @SuppressWarnings("unused")
    public void setEscritorioOrigem(String escritorioOrigem) {
        this.escritorioOrigem = escritorioOrigem;
    }

    /**
     * @return the Municipio
     */
    public String getMunicipio() {
        return municipio;
    }

    /**
     * @param municipio the municipio to set
     */
    @SuppressWarnings("unused")
    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    /**
     * @return the estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * @param estado the estado to set
     */
    @SuppressWarnings("unused")
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    @SuppressWarnings("unused")
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the statusAtual
     */
    public String getStatusAtual() {
        return statusAtual;
    }

    /**
     * @param statusAtual the statusAtual to set
     */
    @SuppressWarnings("unused")
    public void setStatusAtual(String statusAtual) {
        this.statusAtual = statusAtual;
    }

    /**
     * @return the tipoAlocacao
     */
    public String getTipoAlocacao() {
        return tipoAlocacao;
    }

    /**
     * @param tipoAlocacao the tipoAlocacao to set
     */
    @SuppressWarnings("unused")
    public void setTipoAlocacao(String tipoAlocacao) {
        this.tipoAlocacao = tipoAlocacao;
    }

    /**
     * @return Retorna um objeto do tipo <code>Colaborador</code>
     */
    public Colaborador convertDTOToEntity() {

        Colaborador colaborador = new Colaborador();

        colaborador.setCargo(new Cargo(this.getCargoDesc()));
        colaborador.setCpfCnpj(this.getCpfCnpj());
        colaborador.setEmail(this.getEmail());
        colaborador.setEscritorioOrigem(this.getEscritorioOrigem());
        colaborador.setEstado(this.getEstado());
        colaborador.setMunicipio(this.getMunicipio());
        colaborador.setNome(this.getNome());
        colaborador.setStatusAtual(this.getStatusAtual());
        colaborador.setTipoAlocacao(this.getTipoAlocacao());

        return colaborador;
    }


}
