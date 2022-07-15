package br.cnac.analytics.service.interfaces.monitoramento;

import br.cnac.analytics.service.model.monitoramento.Monitoramento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MonitoramentoRepository extends JpaRepository<Monitoramento, Long> {

    @Query(nativeQuery = true, value = "EXEC monitoramento @ano = :ano")
    List<MonitoramentoRel> obtemRelatorio(@Param("ano") String anoBase);


}
