package br.cnac.analytics.domain.dao.colaborador;

import br.cnac.analytics.api.atividade.AtividadeController;
import br.cnac.analytics.domain.dto.filter.FilterDTO;
import br.cnac.analytics.service.interfaces.colaborador.ColaboradorRepository;
import br.cnac.analytics.service.interfaces.colaborador.ColaboradorSimple;
import br.cnac.analytics.service.model.colaborador.Colaborador;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Repository
public class ColaboradorDAO {


    private static final String ANO_BASE_RECORD_FILTER = "anoBaseRecordFilter";
    /**
     * Definição dos filtros da entidade
     */
    private static final String ANO_BASE = "anoBase";
    private static final String ESCRITORIO = "offices";
    private static final String TIPO_SERVICO = "tipoServico";

    private final ColaboradorRepository colaboradorRepository;

    private final EntityManager entityManager;

    public ColaboradorDAO(ColaboradorRepository colaboradorRepository, EntityManager entityManager) {
        this.colaboradorRepository = colaboradorRepository;
        this.entityManager = entityManager;
    }

    public void save(Colaborador colaborador) {
        colaboradorRepository.save(colaborador);
    }

    public List<Colaborador> findAll() {
        return colaboradorRepository.findAll();
    }

    public List<Colaborador> findAllByFilters(FilterDTO filterDTO) {

        Session session = entityManager.unwrap(Session.class);

        if (filterDTO.getEscritorios() != null) {
            if (filterDTO.getEscritorios().length < 3)
                session.enableFilter("officeRecordFilter").setParameterList(ESCRITORIO, filterDTO.getEscritorios());
        }

        if (filterDTO.getTipoServico() != null)
            session.enableFilter("tipoServicoRecordFilter").setParameterList(TIPO_SERVICO, filterDTO.getTipoServico());

        if (filterDTO.getAnoBase() == null)
            filterDTO.setAnoBase(AtividadeController.getBiggerYear().get(0));


        session.enableFilter(ANO_BASE_RECORD_FILTER).setParameter(ANO_BASE, filterDTO.getAnoBase());

        List<Colaborador> colaboradores = new ArrayList<>(colaboradorRepository.findAll());

        if (filterDTO.getCargos() != null)
            colaboradores.removeIf(c -> !List.of(filterDTO.getCargos()).contains(c.getCargo().getCargoDesc()));

        if (filterDTO.getGestores() != null) {
            colaboradores.forEach(c -> c.filtraGestores(filterDTO.getGestores()));
            return colaboradores.stream().filter(c -> !c.getProgramacoes().isEmpty())
                    .toList();
        }

        if (filterDTO.getEscritorioColaborador() != null) {
            return colaboradores.stream().filter(c -> c.contemEscritorioOrigem(filterDTO.getEscritorioColaborador()))
                    .toList();
        }

        return colaboradores;
    }

    public Set<ColaboradorSimple> findAllSimple() {
        return colaboradorRepository.findAllColaboradorSimple();
    }

    public List<Colaborador> findAny(List<String> colaboradores, String anoBase) {

        Session session = entityManager.unwrap(Session.class);

        session.enableFilter(ANO_BASE_RECORD_FILTER).setParameterList(ANO_BASE, Collections.singleton(anoBase));

        return colaboradorRepository.findAllById(colaboradores);
    }

    public Colaborador findOne(String cpfCnpj, String anoBase) {

        Session session = entityManager.unwrap(Session.class);
        session.enableFilter(ANO_BASE_RECORD_FILTER).setParameterList(ANO_BASE, Collections.singleton(anoBase));

        return colaboradorRepository.getReferenceById(cpfCnpj);
    }

    public void delete(String cpfCnpj) {
        colaboradorRepository.deleteById(cpfCnpj);
    }

    @SuppressWarnings("unused")
    public Set<ColaboradorSimple> findAllGestoresSimple() {
        return colaboradorRepository.findAllGestoresSimple();
    }

}
