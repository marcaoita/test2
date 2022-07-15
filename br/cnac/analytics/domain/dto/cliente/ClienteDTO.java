package br.cnac.analytics.domain.dto.cliente;

import br.cnac.analytics.service.model.cliente.Cliente;

public class ClienteDTO {

    private String cnpjCliente;
    private String numCoop;
    private String siglaCoop;
    private String razaoSocial;
    private String codCentral;
    private String siglaCentral;
    private String codSistema;
    private boolean sisbr;
    private String segmentacao;
    private String classe;
    private String municipio;
    private String estado;
    private boolean associado;
    private int diasEnvAmostra;

    private String cor;

    public String getCor() {
        return cor;
    }

    @SuppressWarnings("unused")
    public void setCor(String cor) {
        this.cor = cor;
    }

    public int getDiasEnvAmostra() {
        return diasEnvAmostra;
    }

    @SuppressWarnings("unused")
    public void setDiasEnvAmostra(int diasEnvAmostra) {
        this.diasEnvAmostra = diasEnvAmostra;
    }

    /**
     * @return the associado
     */
    public boolean isAssociado() {
        return associado;
    }

    /**
     * @param associado the associado to set
     */
    @SuppressWarnings("unused")
    public void setAssociado(boolean associado) {
        this.associado = associado;
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

        if (cnpjCliente.length() < 8)
            throw new IllegalArgumentException("Cnpj inválido! Mínimo de 8 caracteres são necessários.");

        this.cnpjCliente = cnpjCliente.replaceAll("\\D+", "");
    }

    /**
     * @return the numCoop
     */
    public String getNumCoop() {
        return numCoop;
    }

    /**
     * @param numCoop the numCoop to set
     */
    @SuppressWarnings("unused")
    public void setNumCoop(String numCoop) {

        if (numCoop.length() < 4)
            throw new IllegalArgumentException("Número da coop de conter ao menos 4 digitos!");

        this.numCoop = numCoop;
    }

    /**
     * @return the siglaCoop
     */
    public String getSiglaCoop() {
        return siglaCoop;
    }

    /**
     * @param siglaCoop the siglaCoop to set
     */
    @SuppressWarnings("unused")
    public void setSiglaCoop(String siglaCoop) {
        this.siglaCoop = siglaCoop;
    }

    /**
     * @return the razaoSocial
     */
    public String getRazaoSocial() {
        return razaoSocial;
    }

    /**
     * @param razaoSocial the razaoSocial to set
     */
    @SuppressWarnings("unused")
    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    /**
     * @return the codCentral
     */
    public String getCodCentral() {
        return codCentral;
    }

    /**
     * @param codCentral the codCentral to set
     */
    @SuppressWarnings("unused")
    public void setCodCentral(String codCentral) {
        this.codCentral = codCentral;
    }

    /**
     * @return the siglaCentral
     */
    public String getSiglaCentral() {
        return siglaCentral;
    }

    /**
     * @param siglaCentral the siglaCentral to set
     */
    @SuppressWarnings("unused")
    public void setSiglaCentral(String siglaCentral) {
        this.siglaCentral = siglaCentral;
    }

    /**
     * @return the codSistema
     */
    public String getCodSistema() {
        return codSistema;
    }

    /**
     * @param codSistema the codSistema to set
     */
    @SuppressWarnings("unused")
    public void setCodSistema(String codSistema) {
        this.codSistema = codSistema;
    }

    /**
     * @return the sisbr
     */
    public boolean isSisbr() {
        return sisbr;
    }

    /**
     * @param sisbr the sisbr to set
     */
    @SuppressWarnings("unused")
    public void setSisbr(boolean sisbr) {
        this.sisbr = sisbr;
    }

    /**
     * @return the segmentacao
     */
    public String getSegmentacao() {
        return segmentacao;
    }

    /**
     * @param segmentacao the segmentacao to set
     */
    @SuppressWarnings("unused")
    public void setSegmentacao(String segmentacao) {
        this.segmentacao = segmentacao;
    }

    /**
     * @return the classe
     */
    public String getClasse() {
        return classe;
    }

    /**
     * @param classe the classe to set
     */
    @SuppressWarnings("unused")
    public void setClasse(String classe) {
        this.classe = classe;
    }

    /**
     * @return the municipio
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

    public Cliente convertDTOToEntity() {
        Cliente c = new Cliente();

        c.setCnpjCliente(this.getCnpjCliente());
        c.setCodCentral(this.getCodCentral());
        c.setCodSistema(this.getCodSistema());
        c.setEstado(this.getEstado());
        c.setMunicipio(this.getMunicipio());
        c.setNumCoop(this.getNumCoop());
        c.setRazaoSocial(this.getRazaoSocial());
        c.setSegmentacao(this.getSegmentacao());
        c.setSiglaCentral(this.getSiglaCentral());
        c.setSiglaCoop(this.getSiglaCoop());
        c.setSisbr(this.isSisbr());
        c.setClasse(this.getClasse());
        c.setAssociado(this.isAssociado());
        c.setDiasEnvAmostra(this.getDiasEnvAmostra());
        c.setCor(this.getCor());

        return c;
    }

}
