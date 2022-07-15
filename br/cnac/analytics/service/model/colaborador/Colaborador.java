package br.cnac.analytics.service.model.colaborador;

import br.cnac.analytics.api.feriado.FeriadoController;
import br.cnac.analytics.service.model.cargo.Cargo;
import br.cnac.analytics.service.model.programacao.InfProgParalela;
import br.cnac.analytics.service.model.programacao.Programacao;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.context.annotation.Lazy;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Pedro Belo
 */

@SuppressWarnings("NullableProblems")
@Entity
@Table(name = "colaboradores")
@FilterDef(name = "officeRecordFilter", parameters = @ParamDef(name = "offices", type = "string"))
@FilterDef(name = "tipoServicoRecordFilter", parameters = @ParamDef(name = "tipoServico", type = "string"))
@FilterDef(name = "anoBaseRecordFilter", parameters = @ParamDef(name = "anoBase", type = "string"))
@FilterDef(name = "cargoColaboradorFilter", parameters = @ParamDef(name = "cargoColaboradorFilter", type = "string"))
@Filter(name = "cargoColaboradorFilter", condition = "cargo_desc in (:cargos)")
public class Colaborador implements Comparable<Colaborador>, Serializable {

    @Serial
    private static final long serialVersionUID = 1905122041950251207L;
    private static final String REGEX = "\\D+";
    @Id
    @Size(min = 8, max = 14)
    @NotNull
    @CPF
    private String cpfCnpj;
    @Size(max = 255)
    @NotNull
    private String nome;
    @NotNull
    @OneToOne
    @JoinColumn(name = "cargo_desc")
    @Fetch(FetchMode.SELECT)
    @Lazy
    private Cargo cargo;
    @Size(max = 50)
    @NotNull
    private String escritorioOrigem;
    @Size(max = 50)
    @NotNull
    private String municipio;
    @NotNull
    private String estado;
    @Size(max = 50)
    @NotNull
    @Email
    private String email;
    @Size(max = 50)
    @NotNull
    private String statusAtual;
    @Size(max = 50)
    @NotNull
    private String tipoAlocacao;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "colaboradores")
    @Fetch(FetchMode.SUBSELECT)
    @JsonIgnoreProperties({"colaboradores", "gestores", "parecer"})
    @Filter(name = "officeRecordFilter", condition = "escritorio_orig in (:offices)")
    @Filter(name = "tipoServicoRecordFilter", condition = "tipo_servico in (:tipoServico)")
    @Filter(name = "anoBaseRecordFilter", condition = "(year(dt_inicio) in (:anoBase) or (ano_base in (:anoBase)))")
    private List<Programacao> programacoes;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "gestores")
    @Fetch(FetchMode.SUBSELECT)
    @JsonIgnoreProperties({"gestores", "colaboradores", "dtFinalAuditoria", "dtEmissaoRAC", "parecer"})
    private List<Programacao> programacoesGestores = new ArrayList<>();

    /**
     *
     */
    public Colaborador() {
    }

    /**
     * @param cpfCnpj - CPF ou CNPJ
     */
    public Colaborador(@Size(min = 8, max = 14) @NotNull String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    /**
     * Método responsável por filtrar as programações atribuídas aos gestores
     * passados como parâmetros.
     *
     * @param gestores - array com o cpf dos gestores que se deseja filtrar.
     */
    public void filtraGestores(String[] gestores) {
        this.programacoes.removeIf(p -> p.hasGestor(gestores));
    }

    /**
     * @param escritorios - Escritório do gestor em questão.
     * @return true se o colaborador pertencer a algum escritório passado como
     * parâmetro.
     */
    public boolean contemEscritorioOrigem(String[] escritorios) {

        List<String> escritoriosId = Arrays.asList(escritorios);

        return escritoriosId.contains(this.escritorioOrigem);

    }


    /**
     * @return Retorna uma lista de objetos do tipo <code>Programacao<code/> que
     * estão intercaladas.
     */
    @SuppressWarnings("unused")
    @JsonIgnoreProperties({"gestores", "colaboradores", "dtFinalAuditoria", "dtEmissaoRAC", "dtPrevAmostra"})
    public Set<Programacao> getProgramacoesParalelas() {

        if (this.programacoes == null)
            return Collections.emptySet();

        Collections.sort(this.programacoes);
        Set<Programacao> paralelas = new HashSet<>();

        for (int i = 0; i < this.programacoes.size(); i++) {

            if (i != (this.programacoes.size() - 1)) {

                if (this.programacoes.get(i).getDtFim().after(this.programacoes.get(i + 1).getDtInicio())) {
                    paralelas.add(this.programacoes.get(i));
                }
            }

            if (i > 0) {

                if (this.programacoes.get(i - 1).getDtFim().after(this.programacoes.get(i).getDtInicio())) {
                    paralelas.add(this.programacoes.get(i));
                }
            }

        }

        return paralelas.stream().sorted(Programacao::compareTo).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * @return Retorna um objeto do tipo <code>InfProgParalela<code/> com
     * informações a serem descontadas das horas alocadas.
     */

    public Set<InfProgParalela> getInfDescontoParalelo() {

        if (this.programacoes == null)
            return Collections.emptySet();

        Collections.sort(this.programacoes);
        Set<InfProgParalela> dadosParalelos = new HashSet<>();

        for (int i = 0; i < this.programacoes.size(); i++) {

            if (i != (this.programacoes.size() - 1)) {

                if (this.programacoes.get(i).getDtFim().after(this.programacoes.get(i + 1).getDtInicio())) {
                    dadosParalelos.add(getHorasDescontarParalela(this.programacoes.get(i).getId(),
                            this.programacoes.get(i + 1).getDtInicio(),
                            this.programacoes.get(i).getDtFim(),
                            this.getEstado(),
                            this.getMunicipio()));

                }
            }

            if (i > 0) {

                if (this.programacoes.get(i - 1).getDtFim().after(this.programacoes.get(i).getDtInicio())) {
                    dadosParalelos.add(getHorasDescontarParalela(this.programacoes.get(i).getId(),
                            this.programacoes.get(i).getDtInicio(),
                            this.programacoes.get(i - 1).getDtFim(),
                            this.getEstado(),
                            this.getMunicipio()));

                }
            }

        }

        return dadosParalelos;
    }

    /**
     * @param idProgramacao - ID da programação.
     * @param dtInicial     - Data de início.
     * @param dtFim         - Data Final
     * @param estado        - Estado
     * @param municipio     - Município
     * @return Retorna quantas horas deverão ser descontadas do total em função de
     * paralelismo de programacoes
     */
    private InfProgParalela getHorasDescontarParalela(Long idProgramacao, Date dtInicial, Date dtFim, String estado,
                                                      String municipio) {

        Calendar dataInicial = Calendar.getInstance();
        Calendar dataFinal = Calendar.getInstance();
        dataInicial.setTime(dtInicial);
        dataFinal.setTime(dtFim);
        InfProgParalela infProgParalela = new InfProgParalela();

        long diasUteis = 0;
        long feriadosDescontados = FeriadoController.findInterval(dtInicial, dtFim, estado, municipio).size();

        for (; !dataInicial.after(dataFinal); dataInicial.add(Calendar.DATE, 1)) {

            int diaDaSemana = dataInicial.get(Calendar.DAY_OF_WEEK);

            if (diaDaSemana != Calendar.SATURDAY && diaDaSemana != Calendar.SUNDAY)
                diasUteis++;

        }

        infProgParalela.setIdProgramacao(idProgramacao);
        infProgParalela.setHorasDesconto((diasUteis - feriadosDescontados) * 4L);

        return infProgParalela;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj.replaceAll(REGEX, "");
    }

    @SuppressWarnings("unused")
    public List<Programacao> getProgramacoesGestores() {
        return programacoesGestores;
    }

    @SuppressWarnings("unused")
    public void setProgramacoesGestores(List<Programacao> programacoesGestores) {
        this.programacoesGestores = programacoesGestores;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    @SuppressWarnings("unused")
    public String getEscritorioOrigem() {
        return escritorioOrigem;
    }

    public void setEscritorioOrigem(String escritorioOrigem) {
        this.escritorioOrigem = escritorioOrigem;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {

        this.email = email;

    }

    public String getStatusAtual() {
        return statusAtual;
    }

    public void setStatusAtual(String statusAtual) {
        this.statusAtual = statusAtual;
    }

    @SuppressWarnings("unused")
    public String getTipoAlocacao() {
        return tipoAlocacao;
    }

    public void setTipoAlocacao(String tipoAlocacao) {
        this.tipoAlocacao = tipoAlocacao;
    }

    public List<Programacao> getProgramacoes() {
        Collections.sort(this.programacoes);
        return this.programacoes;
    }

    @SuppressWarnings("unused")
    public void setProgramacoes(List<Programacao> programacoes) {
        this.programacoes = programacoes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpfCnpj);
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {

        String[] partes = getNome().split(" ");
        return partes[0] + " " + partes[partes.length -1];
    }


    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Colaborador other)) {
            return false;
        }
        return Objects.equals(cpfCnpj, other.cpfCnpj);
    }

    @Override
    public int compareTo(Colaborador o) {

        return Comparator.comparing(Colaborador::getNome)
                .compare(this, o);
    }
}
