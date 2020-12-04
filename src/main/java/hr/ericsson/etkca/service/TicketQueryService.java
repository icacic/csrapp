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

import hr.ericsson.etkca.domain.Ticket;
import hr.ericsson.etkca.domain.*; // for static metamodels
import hr.ericsson.etkca.repository.TicketRepository;
import hr.ericsson.etkca.repository.search.TicketSearchRepository;
import hr.ericsson.etkca.service.dto.TicketCriteria;
import hr.ericsson.etkca.service.dto.TicketDTO;
import hr.ericsson.etkca.service.mapper.TicketMapper;

/**
 * Service for executing complex queries for {@link Ticket} entities in the database.
 * The main input is a {@link TicketCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TicketDTO} or a {@link Page} of {@link TicketDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TicketQueryService extends QueryService<Ticket> {

    private final Logger log = LoggerFactory.getLogger(TicketQueryService.class);

    private final TicketRepository ticketRepository;

    private final TicketMapper ticketMapper;

    private final TicketSearchRepository ticketSearchRepository;

    public TicketQueryService(TicketRepository ticketRepository, TicketMapper ticketMapper, TicketSearchRepository ticketSearchRepository) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
        this.ticketSearchRepository = ticketSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TicketDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TicketDTO> findByCriteria(TicketCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Ticket> specification = createSpecification(criteria);
        return ticketMapper.toDto(ticketRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TicketDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TicketDTO> findByCriteria(TicketCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ticket> specification = createSpecification(criteria);
        return ticketRepository.findAll(specification, page)
            .map(ticketMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TicketCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Ticket> specification = createSpecification(criteria);
        return ticketRepository.count(specification);
    }

    /**
     * Function to convert {@link TicketCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Ticket> createSpecification(TicketCriteria criteria) {
        Specification<Ticket> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Ticket_.id));
            }
            if (criteria.getRbr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRbr(), Ticket_.rbr));
            }
            if (criteria.getAttachmentsId() != null) {
                specification = specification.and(buildSpecification(criteria.getAttachmentsId(),
                    root -> root.join(Ticket_.attachments, JoinType.LEFT).get(Attachment_.id)));
            }
            if (criteria.getStatusId() != null) {
                specification = specification.and(buildSpecification(criteria.getStatusId(),
                    root -> root.join(Ticket_.status, JoinType.LEFT).get(TicketStatus_.id)));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(buildSpecification(criteria.getCategoryId(),
                    root -> root.join(Ticket_.category, JoinType.LEFT).get(Category_.id)));
            }
            if (criteria.getPriorityId() != null) {
                specification = specification.and(buildSpecification(criteria.getPriorityId(),
                    root -> root.join(Ticket_.priority, JoinType.LEFT).get(Priority_.id)));
            }
            if (criteria.getUsersId() != null) {
                specification = specification.and(buildSpecification(criteria.getUsersId(),
                    root -> root.join(Ticket_.users, JoinType.LEFT).get(HDUser_.id)));
            }
            if (criteria.getProjectId() != null) {
                specification = specification.and(buildSpecification(criteria.getProjectId(),
                    root -> root.join(Ticket_.project, JoinType.LEFT).get(Project_.id)));
            }
        }
        return specification;
    }
}
