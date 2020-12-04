package hr.ericsson.etkca.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import hr.ericsson.etkca.domain.Priority;
import hr.ericsson.etkca.domain.*; // for static metamodels
import hr.ericsson.etkca.repository.PriorityRepository;
import hr.ericsson.etkca.repository.search.PrioritySearchRepository;
import hr.ericsson.etkca.service.dto.PriorityCriteria;
import hr.ericsson.etkca.service.dto.PriorityDTO;
import hr.ericsson.etkca.service.mapper.PriorityMapper;

/**
 * Service for executing complex queries for {@link Priority} entities in the database.
 * The main input is a {@link PriorityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PriorityDTO} or a {@link Page} of {@link PriorityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PriorityQueryService extends QueryService<Priority> {

    private final Logger log = LoggerFactory.getLogger(PriorityQueryService.class);

    private final PriorityRepository priorityRepository;

    private final PriorityMapper priorityMapper;

    private final PrioritySearchRepository prioritySearchRepository;

    public PriorityQueryService(PriorityRepository priorityRepository, PriorityMapper priorityMapper, PrioritySearchRepository prioritySearchRepository) {
        this.priorityRepository = priorityRepository;
        this.priorityMapper = priorityMapper;
        this.prioritySearchRepository = prioritySearchRepository;
    }

    /**
     * Return a {@link List} of {@link PriorityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PriorityDTO> findByCriteria(PriorityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Priority> specification = createSpecification(criteria);
        return priorityMapper.toDto(priorityRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PriorityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PriorityDTO> findByCriteria(PriorityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Priority> specification = createSpecification(criteria);
        return priorityRepository.findAll(specification, page)
            .map(priorityMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PriorityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Priority> specification = createSpecification(criteria);
        return priorityRepository.count(specification);
    }

    /**
     * Function to convert {@link PriorityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Priority> createSpecification(PriorityCriteria criteria) {
        Specification<Priority> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Priority_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Priority_.name));
            }
        }
        return specification;
    }
}
