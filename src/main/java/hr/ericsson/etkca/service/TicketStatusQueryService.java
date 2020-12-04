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

import hr.ericsson.etkca.domain.TicketStatus;
import hr.ericsson.etkca.domain.*; // for static metamodels
import hr.ericsson.etkca.repository.TicketStatusRepository;
import hr.ericsson.etkca.repository.search.TicketStatusSearchRepository;
import hr.ericsson.etkca.service.dto.TicketStatusCriteria;
import hr.ericsson.etkca.service.dto.TicketStatusDTO;
import hr.ericsson.etkca.service.mapper.TicketStatusMapper;

/**
 * Service for executing complex queries for {@link TicketStatus} entities in the database.
 * The main input is a {@link TicketStatusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TicketStatusDTO} or a {@link Page} of {@link TicketStatusDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TicketStatusQueryService extends QueryService<TicketStatus> {

    private final Logger log = LoggerFactory.getLogger(TicketStatusQueryService.class);

    private final TicketStatusRepository ticketStatusRepository;

    private final TicketStatusMapper ticketStatusMapper;

    private final TicketStatusSearchRepository ticketStatusSearchRepository;

    public TicketStatusQueryService(TicketStatusRepository ticketStatusRepository, TicketStatusMapper ticketStatusMapper, TicketStatusSearchRepository ticketStatusSearchRepository) {
        this.ticketStatusRepository = ticketStatusRepository;
        this.ticketStatusMapper = ticketStatusMapper;
        this.ticketStatusSearchRepository = ticketStatusSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TicketStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TicketStatusDTO> findByCriteria(TicketStatusCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TicketStatus> specification = createSpecification(criteria);
        return ticketStatusMapper.toDto(ticketStatusRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TicketStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TicketStatusDTO> findByCriteria(TicketStatusCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TicketStatus> specification = createSpecification(criteria);
        return ticketStatusRepository.findAll(specification, page)
            .map(ticketStatusMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TicketStatusCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TicketStatus> specification = createSpecification(criteria);
        return ticketStatusRepository.count(specification);
    }

    /**
     * Function to convert {@link TicketStatusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TicketStatus> createSpecification(TicketStatusCriteria criteria) {
        Specification<TicketStatus> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TicketStatus_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TicketStatus_.name));
            }
        }
        return specification;
    }
}
