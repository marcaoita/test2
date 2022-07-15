package br.cnac.analytics.domain.dto.filter;


/**
 * @author Pedro.belo
 * <p>
 * Classe responsável por capturar os filtros padrões requisitados pelo FrontEnd
 */
public class FilterDTO {

    String[] escritorios;
    String[] tipoServico;
    String anoBase;
    String[] gestores;
    String[] escritorioColaborador;
    String amostra;
    Integer[] mes;
    String[] cargos;
    String[] areas;

    String[] status;

    public String[] getStatus() {
        return status;
    }

    public void setStatus(String[] status) {
        this.status = status;
    }

    public String[] getAreas() {
        return areas;
    }

    @SuppressWarnings("unused")
    public void setAreas(String[] areas) {
        this.areas = areas;
    }

    public String getAmostra() {
        return amostra;
    }

    @SuppressWarnings("unused")
    public void setAmostra(String amostra) {
        this.amostra = amostra;
    }

    public String[] getEscritorios() {
        return escritorios;
    }

    @SuppressWarnings("unused")
    public void setEscritorios(String[] escritorios) {
        this.escritorios = escritorios;
    }

    public String[] getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(String[] tipoServico) {
        this.tipoServico = tipoServico;
    }

    public String getAnoBase() {
        return anoBase;
    }

    public void setAnoBase(String anoBase) {
        this.anoBase = anoBase;
    }

    public String[] getGestores() {
        return gestores;
    }

    @SuppressWarnings("unused")
    public void setGestores(String[] gestores) {
        this.gestores = gestores;
    }

    public String[] getEscritorioColaborador() {
        return escritorioColaborador;
    }

    @SuppressWarnings("unused")
    public void setEscritorioColaborador(String[] escritorioColaborador) {
        this.escritorioColaborador = escritorioColaborador;
    }

    public Integer[] getMes() {
        return mes;
    }

    @SuppressWarnings("unused")
    public void setMes(Integer[] mes) {
        this.mes = mes;
    }

    public String[] getCargos() {
        return cargos;
    }

    @SuppressWarnings("unused")
    public void setCargos(String[] cargos) {
        this.cargos = cargos;
    }
}
