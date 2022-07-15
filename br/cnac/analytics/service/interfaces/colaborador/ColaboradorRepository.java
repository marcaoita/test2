package br.cnac.analytics.service.interfaces.colaborador;

import br.cnac.analytics.service.model.colaborador.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface ColaboradorRepository extends JpaRepository<Colaborador, String>, JpaSpecificationExecutor<Colaborador> {

    @Query(nativeQuery = true, value = "select c.cpf_cnpj as cpfCnpj, c.nome as nome, c.cargo_desc as cargo, CA.area as area, c.escritorio_origem as escritorioOrigem, c.email as email " +
            "from colaboradores c " +
            "inner join dbo.cargos ca on ca.cargo_desc = C.cargo_desc " +
            "where c.status_atual = 'Ativo'")
    Set<ColaboradorSimple> findAllColaboradorSimple();

    @Query(nativeQuery = true, value = "select c.cpf_cnpj as cpfCnpj, c.nome as nome, c.cargo_desc as cargo, CA.area as area, c.escritorio_origem as escritorioOrigem, c.email as email " +
            "from colaboradores c " +
            "inner join dbo.cargos ca on ca.cargo_desc = C.cargo_desc " +
            "where ca.area in (N'Supervisão', N'Gerência', N'Diretoria Executiva') and c.status_atual = 'Ativo'")
    Set<ColaboradorSimple> findAllGestoresSimple();

}
