package br.cnac.analytics.service.model.escopo;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

@Entity
@Table(name = "escopos")
public class Escopo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1905122041950251207L;
    @Id
    @Size(min = 3)
    @NotNull
    private String numEscopo;
    @Size(max = 255)
    @NotNull
    private String tipoEscopo;
    @Size(max = 255)
    @NotNull
    private String aplicacao;
    private String escopoAgregador;

    private String apelido;


    public Escopo() {
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    /**
     * @param numEscopo - Número do escopo
     */
    public Escopo(@Size(min = 3) @NotNull String numEscopo) {
        this.numEscopo = numEscopo;
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
    public void setEscopoAgregador(String escopoAgregador) {
        this.escopoAgregador = escopoAgregador;
    }

    public String getNumEscopo() {
        return numEscopo;
    }

    public void setNumEscopo(String numEscopo) {

        if (numEscopo.length() < 3)
            throw new IllegalArgumentException("Número de escopo deve ter ao menos 3 dígitos!");

        this.numEscopo = numEscopo;

    }

    @SuppressWarnings("unused")
    public String getTipoEscopo() {
        return tipoEscopo;
    }

    public void setTipoEscopo(String tipoEscopo) {
        this.tipoEscopo = tipoEscopo;
    }

    @SuppressWarnings("unused")
    public String getAplicacao() {
        return aplicacao;
    }

    public void setAplicacao(String aplicacao) {
        this.aplicacao = aplicacao;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */

    @Override
    public String toString() {
        return this.apelido;
    }


}
