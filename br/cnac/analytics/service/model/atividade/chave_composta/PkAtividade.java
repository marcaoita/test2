package br.cnac.analytics.service.model.atividade.chave_composta;

import javax.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PkAtividade implements Serializable {

    /**
     *
     */
    public PkAtividade() {
    }

    /**
     * @param cnpjCliente - CNPJ do cliente
     * @param numEscopo   - Número do escopo
     * @param anoBase     - Ano Base
     */
    public PkAtividade(String cnpjCliente, String numEscopo, String anoBase) {
        this.cnpjCliente = cnpjCliente;
        this.numEscopo = numEscopo;
        this.anoBase = anoBase;
    }

    @Serial
    private static final long serialVersionUID = 1L;

    protected String cnpjCliente;

    protected String numEscopo;

    protected String anoBase;


    /**
     * @return the anoBase
     */
    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
    public String getCnpjCliente() {
        return cnpjCliente;
    }

    @SuppressWarnings("unused")
    public void setCnpjCliente(String cnpjCliente) {
        this.cnpjCliente = cnpjCliente;
    }

    @SuppressWarnings("unused")
    public String getNumEscopo() {
        return numEscopo;
    }

    @SuppressWarnings("unused")
    public void setNumEscopo(String numEscopo) {
        this.numEscopo = numEscopo;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */

    @Override
    public int hashCode() {
        return Objects.hash(anoBase, cnpjCliente, numEscopo);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PkAtividade other)) {
            return false;
        }
        return Objects.equals(anoBase, other.anoBase) && Objects.equals(cnpjCliente, other.cnpjCliente)
                && Objects.equals(numEscopo, other.numEscopo);
    }

    @Override
    public String toString() {
        return "PkAtividade{" +
                "cnpjCliente='" + cnpjCliente + '\'' +
                ", numEscopo='" + numEscopo + '\'' +
                ", anoBase='" + anoBase + '\'' +
                '}';
    }
}
