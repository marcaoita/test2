package br.cnac.analytics.service.interfaces.cliente;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.cnac.analytics.service.model.cliente.Cliente;
import br.cnac.analytics.service.interfaces.relatorio.ControleCSA;

public interface ClienteRepository extends JpaRepository<Cliente, String> {

    @SuppressWarnings("unused")
    @Query("SELECT obj from Cliente obj JOIN FETCH obj.programacoes")
    Set<Cliente> findAllFetch();

    @Query(nativeQuery = true, value = "select c.cnpj_cliente as cnpjCliente, c.num_coop as numCoop, c.sigla_coop as siglaCoop from clientes c")
    Set<ClientSimple> findAllClientSimple();

    @Query(nativeQuery = true, value = "select C.cnpj_cliente as cnpjCliente, C.cod_central as codCentral, C.num_coop as numCoop, C.sigla_coop as siglaCoop, A.ano_base as anoBase, sum(A.horas_vendidas) as horasVendidas, A.tipo_servico as tipoServico, A.fk_num_escopo as numEscopo, E.tipo_escopo as tipoEscopo "
            + "from clientes C "
            + "inner join dbo.atividades A on A.fk_cnpj_cliente = C.cnpj_cliente "
            + "inner join dbo.escopos E on A.fk_num_escopo = E.num_escopo "
            + "left join dbo.atividades_alocadas P on (P.atividades_fk_cnpj_cliente = A.fk_cnpj_cliente and P.atividades_ano_base = A.ano_base and A.fk_num_escopo = P.atividades_fk_num_escopo) "
            + "where P.fk_programacao is null and A.status_atividade = 'ATIVA'"
            + "group by C.cnpj_cliente, C.cod_central, C.num_coop, C.sigla_coop, A.tipo_servico, A.ano_base, A.fk_num_escopo, E.tipo_escopo "
            + "order by C.sigla_coop")
    Set<ClienteSemProgramacaoRepository> findClientesSemProgramacao();

    @Query(nativeQuery = true, value = "EXEC controleCSA @anoBase = :ano_base")
    List<ControleCSA> obtemRelatorio(@Param("ano_base") String anoBase);

}
