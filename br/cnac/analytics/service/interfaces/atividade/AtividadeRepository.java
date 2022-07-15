package br.cnac.analytics.service.interfaces.atividade;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.cnac.analytics.service.model.atividade.chave_composta.PkAtividade;
import br.cnac.analytics.service.model.atividade.entity.Atividade;

import java.util.List;

public interface AtividadeRepository extends JpaRepository<Atividade, PkAtividade> {


    @Query(nativeQuery = true, value = "select a.ano_base as anoBase from atividades a group by ano_base")
    List<AtividadeSimple> findAllAtividadeSimple();

}
