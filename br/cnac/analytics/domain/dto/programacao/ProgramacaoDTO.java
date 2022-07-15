package br.cnac.analytics.domain.dto.programacao;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.cnac.analytics.service.enumeration.TipoServico;
import br.cnac.analytics.service.model.atividade.chave_composta.PkAtividade;
import br.cnac.analytics.service.model.atividade.entity.Atividade;
import br.cnac.analytics.service.model.cargo.Cargo;
import br.cnac.analytics.service.model.cliente.Cliente;
import br.cnac.analytics.service.model.colaborador.Colaborador;
import br.cnac.analytics.service.model.programacao.Programacao;

public class ProgramacaoDTO {

    private String id;
    private String anoBase;
    private String tipoServico;
    private String cnpjCliente;
    private String dtInicio;
    private String dtFim;
    private String dtInicioExe;
    private String dtFimExe;
    private String[] gestoresIds;
    private String[] emailGestores;
    private String[] areaGestores;
    private String[] colaboradoresIds;
    private String[] emailColaboradores;
    private String[] areaColaboradores;
    private String[] escoposAtividades;
    private String[] horasAtividades;
    private String tipoVisita;
    private String escritorio;
    private boolean temCSA;
    private int atraso;
    private boolean amostraGerada;
    private String dtBase;


    /**
     * @return the amostraGerada
     */
    public boolean isAmostraGerada() {
        return amostraGerada;
    }

    /**
     * @param amostraGerada the amostraGerada to set
     */
    @SuppressWarnings("unused")
    public void setAmostraGerada(boolean amostraGerada) {
        this.amostraGerada = amostraGerada;
    }

    /**
     * @return the dtBase
     */
    public String getDtBase() {
        return dtBase;
    }

    /**
     * @param dtBase the dtBase to set
     */
    @SuppressWarnings("unused")
    public void setDtBase(String dtBase) {
        this.dtBase = dtBase;
    }

    /**
     * @return the areaGestores
     */
    public String[] getAreaGestores() {
        return areaGestores;
    }

    /**
     * @param areaGestores the areaGestores to set
     */
    @SuppressWarnings("unused")
    public void setAreaGestores(String[] areaGestores) {
        this.areaGestores = areaGestores;
    }

    /**
     * @return the areaColaboradores
     */
    public String[] getAreaColaboradores() {
        return areaColaboradores;
    }

    /**
     * @param areaColaboradores the areaColaboradores to set
     */
    @SuppressWarnings("unused")
    public void setAreaColaboradores(String[] areaColaboradores) {
        this.areaColaboradores = areaColaboradores;
    }

    /**
     * @return the emailGestores
     */
    public String[] getEmailGestores() {
        return emailGestores;
    }

    /**
     * @param emailGestores the emailGestores to set
     */
    @SuppressWarnings("unused")
    public void setEmailGestores(String[] emailGestores) {
        this.emailGestores = emailGestores;
    }

    /**
     * @return the emailColaboradores
     */
    public String[] getEmailColaboradores() {
        return emailColaboradores;
    }

    /**
     * @param emailColaboradores the emailColaboradores to set
     */
    @SuppressWarnings("unused")
    public void setEmailColaboradores(String[] emailColaboradores) {
        this.emailColaboradores = emailColaboradores;
    }

    /**
     * @return the atraso
     */
    public int getAtraso() {
        return atraso;
    }

    /**
     * @param atraso the atraso to set
     */
    @SuppressWarnings("unused")
    public void setAtraso(int atraso) {
        this.atraso = atraso;
    }

    /**
     * @return the temCSA
     */
    public boolean isTemCSA() {
        return temCSA;
    }

    /**
     * @param temCSA the temCSA to set
     */
    @SuppressWarnings("unused")
    public void setTemCSA(boolean temCSA) {
        this.temCSA = temCSA;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    @SuppressWarnings("unused")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the escritorio
     */
    public String getEscritorio() {
        return escritorio;
    }

    /**
     * @param escritorio the escritorio to set
     */
    @SuppressWarnings("unused")
    public void setEscritorio(String escritorio) {
        this.escritorio = escritorio;
    }

    /**
     * @return the escoposAtividades
     */
    public String[] getEscoposAtividades() {
        return escoposAtividades;
    }

    /**
     * @param escoposAtividades the escoposAtividades to set
     */
    @SuppressWarnings("unused")
    public void setEscoposAtividades(String[] escoposAtividades) {
        this.escoposAtividades = escoposAtividades;
    }

    /**
     * @return the horasAtividades
     */
    @SuppressWarnings("unused")
    public String[] getHorasAtividades() {
        return horasAtividades;
    }

    /**
     * @param horasAtividades the horasAtividades to set
     */
    @SuppressWarnings("unused")
    public void setHorasAtividades(String[] horasAtividades) {
        this.horasAtividades = horasAtividades;
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
     * @return the dtInicio
     */
    public String getDtInicio() {
        return dtInicio;
    }

    /**
     * @param dtInicio the dtInicio to set
     */
    @SuppressWarnings("unused")
    public void setDtInicio(String dtInicio) {
        this.dtInicio = dtInicio;
    }

    /**
     * @return the dtFim
     */
    public String getDtFim() {
        return dtFim;
    }

    /**
     * @param dtFim the dtFim to set
     */
    @SuppressWarnings("unused")
    public void setDtFim(String dtFim) {
        this.dtFim = dtFim;
    }

    /**
     * @return the dtInicioExe
     */
    public String getDtInicioExe() {
        return dtInicioExe;
    }

    /**
     * @param dtInicioExe the dtInicioExe to set
     */
    @SuppressWarnings("unused")
    public void setDtInicioExe(String dtInicioExe) {
        this.dtInicioExe = dtInicioExe;
    }

    /**
     * @return the dtFimExe
     */
    public String getDtFimExe() {
        return dtFimExe;
    }

    /**
     * @param dtFimExe the dtFimExe to set
     */
    @SuppressWarnings("unused")
    public void setDtFimExe(String dtFimExe) {
        this.dtFimExe = dtFimExe;
    }

    /**
     * @return the gestoresIds
     */
    public String[] getGestoresIds() {
        return gestoresIds;
    }

    /**
     * @param gestoresIds the gestoresIds to set
     */
    @SuppressWarnings("unused")
    public void setGestoresIds(String[] gestoresIds) {
        this.gestoresIds = gestoresIds;
    }

    /**
     * @return the colaboradoresIds
     */
    public String[] getColaboradoresIds() {
        return colaboradoresIds;
    }

    /**
     * @param colaboradoresIds the colaboradoresIds to set
     */
    @SuppressWarnings("unused")
    public void setColaboradoresIds(String[] colaboradoresIds) {
        this.colaboradoresIds = colaboradoresIds;
    }

    /**
     * @return the tipoVisita
     */
    public String getTipoVisita() {
        return tipoVisita;
    }

    /**
     * @param tipoVisita the tipoVisita to set
     */
    @SuppressWarnings("unused")
    public void setTipoVisita(String tipoVisita) {
        this.tipoVisita = tipoVisita;
    }

    /**
     * @return Retorna um objeto do tipo <code>Programacao<code/>
     */
    public Programacao convertDTOToEntity() {

        Programacao p = new Programacao();
        Set<Colaborador> gestores = new HashSet<>();
        Set<Colaborador> colaboradores = new HashSet<>();
        List<Atividade> atividades = new ArrayList<>();

        for (int i = 0; i < this.getColaboradoresIds().length; i++) {

            Colaborador c = new Colaborador();
            c.setCpfCnpj(this.getColaboradoresIds()[i]);
            c.setEmail(this.getEmailColaboradores()[i]);
            Cargo car = new Cargo();
            car.setArea(this.getAreaColaboradores()[i]);
            c.setCargo(car);
            colaboradores.add(c);
        }

        for (int i = 0; i < this.getGestoresIds().length; i++) {
            Colaborador c = new Colaborador();
            c.setCpfCnpj(this.getGestoresIds()[i]);
            c.setEmail(this.getEmailGestores()[i]);
            Cargo car = new Cargo();
            car.setArea(this.getAreaGestores()[i]);
            c.setCargo(car);
            gestores.add(c);
        }

        TipoServico servico = TipoServico.valueOf(this.getTipoServico());

        if (TipoServico.getServicosOperacionais().contains(servico)) {

            for (int i = 0; i < this.getEscoposAtividades().length; i++) {
                Atividade a = new Atividade();
                a.setPkAtividade(
                        new PkAtividade(this.getCnpjCliente(), this.getEscoposAtividades()[i],
                                this.getAnoBase()));
                atividades.add(a);
            }

            p.setAtividades(atividades);
        } else {
            p.setAtividades(null);
        }

        if (this.getId() != null)
            p.setId(Long.parseLong(this.getId()));

        if (this.getAtraso() > 0)
            p.setDiasAtraso(this.getAtraso());

        p.setAnoBase(this.getAnoBase());
        p.setAtividades(atividades);
        p.setColaboradores(colaboradores);
        p.setCli(new Cliente(this.getCnpjCliente()));
        p.setDtFim(stringToDate(this.getDtFim()));
        p.setDtFimExe(stringToDate(this.getDtFimExe()));
        p.setDtInicio(stringToDate(this.getDtInicio()));
        p.setDtInicioExe(stringToDate(this.getDtInicioExe()));
        p.setGestores(gestores);
        p.setTipoServico(TipoServico.valueOf(this.getTipoServico()));
        p.setTipoVisita(this.getTipoVisita());
        p.setEscritorioOrig(this.getEscritorio());
        p.setTemCSA(this.isTemCSA());
        p.setDiasAtraso(this.getAtraso());
        p.setDtBase(this.getDtBase());
        p.setAmostraGerada(this.isAmostraGerada());

        return p;
    }

    /**
     * @param date - Data no formato DD-MM-YYYY a ser convertida.
     * @return Retorna um objeto do tipo date.
     */
    private Date stringToDate(String date) {

        if (date == null)
            return null;

        if (date.equals(""))
            return null;

        DateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
        try {
            return new Date(formato.parse(date).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


}