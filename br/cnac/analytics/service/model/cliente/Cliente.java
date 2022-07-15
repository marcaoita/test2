package br.cnac.analytics.service.model.cliente;

import br.cnac.analytics.service.enumeration.TipoServico;
import br.cnac.analytics.service.model.atividade.entity.Atividade;
import br.cnac.analytics.service.model.programacao.Programacao;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "clientes")
@FilterDef(name = "anoBaseClientFilter", parameters = @ParamDef(name = "anoBase", type = "string"))
@FilterDef(name = "officeClientFilter", parameters = @ParamDef(name = "offices", type = "string"))
@FilterDef(name = "tipoServicoClientFilter", parameters = @ParamDef(name = "tipoServico", type = "string"))
public class Cliente implements Serializable {
    @Serial
    private static final long serialVersionUID = 1905122049750251207L;
    @Transient
    private static final double DIA = 8.0;
    @Id
    @NotNull
    @Size(min = 8, max = 14)
    private String cnpjCliente;
    @Size(max = 4)
    @NotNull
    private String numCoop;
    @Size(max = 255)
    @NotNull
    private String siglaCoop;
    @Size(max = 255)
    @NotNull
    private String razaoSocial;
    @NotNull
    private String codCentral;
    @Size(max = 255)
    @NotNull
    private String siglaCentral;
    @Size(max = 255)
    @NotNull
    private String codSistema;
    @NotNull
    private boolean sisbr;
    @Size(max = 255)
    @NotNull
    private String segmentacao;
    @Size(max = 255)
    @NotNull
    private String classe;
    @Size(max = 255)
    @NotNull
    private String municipio;
    @Size(max = 255)
    @NotNull
    private String estado;
    @ColumnDefault("1")
    private boolean associado;
    @ColumnDefault("16")
    private int diasEnvAmostra;
    private String cor;
    @OneToMany(mappedBy = "cli", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    @JsonIgnoreProperties({"cli", "parecer"})
    @Filter(name = "officeClientFilter", condition = "escritorio_orig in (:offices)")
    @Filter(name = "tipoServicoClientFilter", condition = "tipo_servico in (:tipoServico)")
    @Filter(name = "anoBaseClientFilter", condition = "ano_base in (:anoBase)")
    private Set<Programacao> programacoes = new HashSet<>();

    /**
     *
     */
    public Cliente() {
    }

    /**
     * @param cnpjCliente - CNPJ do cliente em questão.
     */
    public Cliente(@NotNull @Size(min = 8, max = 14) String cnpjCliente) {
        this.cnpjCliente = cnpjCliente;
    }

    @SuppressWarnings("unused")
    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    /**
     * @param gestores Array com o cpf dos gestores que se deseja filtrar.
     *                 Método responsável por filtrar as programações atribuídas aos gestores
     *                 passados como parâmetros.
     */
    public void filtraGestores(String[] gestores) {

        this.programacoes.removeIf(p -> p.hasGestor(gestores));

    }

    public int getDiasEnvAmostra() {
        return diasEnvAmostra;
    }

    public void setDiasEnvAmostra(int diasEnvAmostra) {
        this.diasEnvAmostra = diasEnvAmostra;
    }


    /**
     * @return Retorna a contagem de avaliações críticas necessárias ao cliente.
     * */
    public long getQuantAvaliacoes(){

        Set<Atividade> atividades = new HashSet<>();

        for (Programacao p : this.programacoes) {
            atividades.addAll(p.getAtividades());
        }

        return atividades.stream().filter(a -> a.getTipoServico().equals(TipoServico.AC)).count();
    }

    /**
     * @return Retorna a contagem de avaliações críticas preenchidas.
     * */
    public long getQuantAvaliacoesPreenchidas(){

        Set<Atividade> atividades = new HashSet<>();

        for (Programacao p : this.programacoes) {
            atividades.addAll(p.getAtividades());
        }

        return atividades.stream().filter(a -> a.getTipoServico().equals(TipoServico.AC) && a.getAvaliacaoCritica() != null).count();
    }

    public String statusAvaliacoes (){

        if(getQuantAvaliacoes() - getQuantAvaliacoesPreenchidas() == 0){
            return "Concluído";
        }

        return "Pendente";
    }



    /**
     * @return Retorna a contagem de escopos alocados ao cliente em questão.
     */
    public int quantEscopos() {
        Set<Atividade> atividades = new HashSet<>();

        for (Programacao p : this.programacoes) {
            atividades.addAll(p.getAtividades());
        }

        return (int) atividades.stream().mapToInt(a -> a.getEscopo().hashCode()).count();
    }

    /**
     * @param tipo tipo de serviço no qual se deseja a quantidade de escopos.
     * @return Retorna a contagem de escopos alocados ao cliente em questão.
     */
    @SuppressWarnings("unused")
    public int quantEscopos(String tipo) {

        Set<Atividade> atividades = new HashSet<>();

        for (Programacao p : this.programacoes) {

            if (p.getTipoServico().equals(TipoServico.valueOf(tipo)))
                atividades.addAll(p.getAtividades());
        }

        return (int) atividades.stream().mapToInt(a -> a.getEscopo().hashCode()).count();
    }

    /**
     * @param tipo informa o tipo de serviço que se deseja somar.
     * @return Retorna a foma de dias contratados por tipo de serviço
     */
    public double diasContratados(String tipo) {

        Set<Atividade> atividades = new HashSet<>();

        for (Programacao p : this.programacoes) {

            if (p.getTipoServico().equals(TipoServico.valueOf(tipo)))
                atividades.addAll(p.getAtividades());
        }

        return Math.round(atividades.stream().mapToDouble(Atividade::getHorasVendidas).sum() / DIA);
    }

    /**
     * @return Retorna a soma de dias contratados
     */
    public double diasContratados() {

        Set<Atividade> atividades = new HashSet<>();

        for (Programacao p : this.programacoes) {
            atividades.addAll(p.getAtividades());
        }

        return Math.round(atividades.stream().mapToDouble(Atividade::getHorasVendidas).sum() / DIA);
    }

    /**
     * @param tipo - informa o tipo de serviço que se deseja somar.
     * @return Retorna a soma de dias alocados por tipo de serviço
     */
    public double diasAlocados(String tipo) {

        return this.programacoes.stream().filter(p -> p.getTipoServico().equals(TipoServico.valueOf(tipo)))
                .mapToDouble(Programacao::getHorasAlocadas).sum() / DIA;

    }

    /**
     * @return Retorna a soma de dias alocados
     */
    public double diasAlocados() {

        return this.programacoes.stream().mapToDouble(Programacao::getHorasAlocadas).sum() / DIA;
    }

    /**
     * @return retorna is dias destinadas a revisão
     */
    public double diasRevisao() {

        return Math.round(this.programacoes.stream().mapToDouble(Programacao::getHorasRevisao).sum() / DIA);
    }

    /**
     * @param tipo - tipo de serviço selecionado.
     * @return retorna os dias destinados a revisão, com base no tipo de serviço
     * passado como parâmetro.
     */
    public double diasRevisao(String tipo) {
        return Math.round(this.programacoes.stream().filter(p -> p.getTipoServico().equals(TipoServico.valueOf(tipo)))
                .mapToDouble(Programacao::getHorasRevisao).sum() / DIA);
    }

    /**
     * @return Retorna os dias destinados ao CSA para o cliente em questão.
     */
    public double diasDestCSA() {

        double horasDestCSA = this.programacoes.stream().mapToDouble(Programacao::getHorasDestCSA).sum();

        if (horasDestCSA == 0)
            return 0.0;

        return Math.round(horasDestCSA / DIA);
    }

    /**
     * @param tipo - tipo de serviço selecionado.
     * @return Retorna os dias destinados ao CSA para o cliente em questão, em função do tipo selecionado.
     */
    public double diasDestCSA(String tipo) {

        double horasDestCSA = this.programacoes.stream()
                .filter(p -> p.getTipoServico().equals(TipoServico.valueOf(tipo)))
                .mapToDouble(Programacao::getHorasDestCSA).sum();

        if (horasDestCSA == 0)
            return 0.0;

        return Math.round(horasDestCSA / DIA);
    }

    /**
     * @return Retorna o saldo de dias. (Dias contratados - Dias alocados - Dias Destinados ao CSA - Dias de Revisão.)
     */
    @SuppressWarnings("unused")
    public double saldoDias() {
        double diasContratados = diasContratados();
        double diasProgramados = diasAlocados() + diasDestCSA() + diasRevisao();

        return diasContratados - diasProgramados;
    }

    /**
     * @param tipo - tipo de serviço selecionado.
     * @return Retorna o saldo de dias em função do tipo. (Dias contratados - Dias alocados - Dias Destinados ao CSA - Dias de Revisão.)
     */

    @SuppressWarnings("unused")
    public double saldoDias(String tipo) {
        double diasContratados = diasContratados(tipo);
        double diasProgramados = diasAlocados(tipo) + diasDestCSA(tipo) + diasRevisao(tipo);

        return diasContratados - diasProgramados;
    }

    /**
     * @return Retorna o percentual de comprometimento dos dias contratados.
     */
    @SuppressWarnings("unused")
    public String comprometimentoDiasContratados() {

        double diasAlocados = (diasAlocados() + diasDestCSA() + diasRevisao());
        double diasContratados = diasContratados();

        if (diasContratados == 0)
            return "0";

        return convertToPercentage(diasAlocados / diasContratados);
    }

    /**
     * @param tipo tipo de serviço desejado.
     * @return Retorna o percentual de comprometimento dos dias contratados em
     * função do tipo.
     */
    @SuppressWarnings("unused")
    public String comprometimentoDiasContratados(String tipo) {

        double diasAlocados = (diasAlocados(tipo) + diasDestCSA(tipo) + diasRevisao(tipo));
        double diasContratados = diasContratados(tipo);

        if (diasContratados == 0)
            return "0";

        return convertToPercentage(diasAlocados / diasContratados);
    }

    /**
     * @param valor valor em decimal que se deseja converter.
     * @return Retorna o valor passado como parâmetro no formato de porcentagem, por
     * exemplo, 0.8515 para 85.15%
     */
    private String convertToPercentage(double valor) {

        DecimalFormat df = new DecimalFormat("#.00%");

        return df.format(valor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cnpjCliente);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Cliente other)) {
            return false;
        }
        return Objects.equals(cnpjCliente, other.cnpjCliente);
    }

    public Set<Programacao> getProgramacoes() {
        return programacoes;
    }

    public void setProgramacoes(Set<Programacao> programacoes) {
        this.programacoes = programacoes;
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
    public void setAssociado(boolean associado) {
        this.associado = associado;
    }

    public String getCnpjCliente() {
        return cnpjCliente;
    }

    public void setCnpjCliente(String cnpjCliente) {

        if (cnpjCliente.length() < 8)
            throw new IllegalArgumentException("Cnpj inválido! Mínimo de 8 caracteres são necessários.");

        this.cnpjCliente = cnpjCliente.replaceAll("\\D+", "");
    }

    public String getNumCoop() {
        return numCoop;
    }

    public void setNumCoop(String numCoop) {

        if (numCoop.length() < 4)
            throw new IllegalArgumentException("Número da coop de conter ao menos 4 dígitos!");
        this.numCoop = numCoop;

    }

    public String getSiglaCoop() {
        return siglaCoop;
    }

    public void setSiglaCoop(String siglaCoop) {
        this.siglaCoop = siglaCoop;
    }

    @SuppressWarnings("unused")
    public String getCodCentral() {
        return codCentral;
    }

    public void setCodCentral(String codCentral) {
        this.codCentral = codCentral;
    }

    @SuppressWarnings("unused")
    public String getSiglaCentral() {
        return siglaCentral;
    }

    public void setSiglaCentral(String siglaCentral) {
        this.siglaCentral = siglaCentral;
    }

    @SuppressWarnings("unused")
    public String getCodSistema() {
        return codSistema;
    }

    public void setCodSistema(String codSistema) {
        this.codSistema = codSistema;
    }

    @SuppressWarnings("unused")
    public boolean isSisbr() {
        return sisbr;
    }

    public void setSisbr(boolean sisbr) {
        this.sisbr = sisbr;
    }

    public String getSegmentacao() {
        return segmentacao;
    }

    public void setSegmentacao(String segmentacao) {
        this.segmentacao = segmentacao;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @SuppressWarnings("unused")
    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

}
