package br.cnac.analytics.service.model.programacao;
/*
 * @author Pedro Belo
 *

 * */
public class InfProgParalela {

    long idProgramacao;
    Long horasDesconto;

    public Long getIdProgramacao() {
        return idProgramacao;
    }

    public void setIdProgramacao(Long idProgramacao) {
        this.idProgramacao = idProgramacao;
    }

    public Long getHorasDesconto() {
        return horasDesconto;
    }

    public void setHorasDesconto(Long horasDesconto) {
        this.horasDesconto = horasDesconto;
    }
}
