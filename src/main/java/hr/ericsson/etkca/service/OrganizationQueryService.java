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

import hr.ericsson.etkca.domain.Organization;
import hr.ericsson.etkca.domain.*; // for static metamodels
import hr.ericsson.etkca.repository.OrganizationRepository;
import hr.ericsson.etkca.repository.search.OrganizationSearchRepository;
import hr.ericsson.etkca.service.dto.OrganizationCriteria;
import hr.ericsson.etkca.service.dto.OrganizationDTO;
import hr.ericsson.etkca.service.mapper.OrganizationMapper;

/**
 * Service for executing complex queries for {@link Organization} entities in the database.
 * The main input is a {@link OrganizationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OrganizationDTO} or a {@link Page} of {@link OrganizationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OrganizationQueryService extends QueryService<Organization> {

    private final Logger log = LoggerFactory.getLogger(OrganizationQueryService.class);

    private final OrganizationRepository organizationRepository;

    private final OrganizationMapper organizationMapper;

    private final OrganizationSearchRepository organizationSearchRepository;

    public OrganizationQueryService(OrganizationRepository organizationRepository, OrganizationMapper organizationMapper, OrganizationSearchRepository organizationSearchRepository) {
        this.organizationRepository = organizationRepository;
        this.organizationMapper = organizationMapper;
        this.organizationSearchRepository = organizationSearchRepository;
    }

    /**
     * Return a {@link List} of {@link OrganizationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OrganizationDTO> findByCriteria(OrganizationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Organization> specification = createSpecification(criteria);
        return organizationMapper.toDto(organizationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OrganizationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OrganizationDTO> findByCriteria(OrganizationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Organization> specification = createSpecification(criteria);
        return organizationRepository.findAll(specification, page)
            .map(organizationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OrganizationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Organization> specification = createSpecification(criteria);
        return organizationRepository.count(specification);
    }

    /**
     * Function to convert {@link OrganizationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Organization> createSpecification(OrganizationCriteria criteria) {
        Specification<Organization> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Organization_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Organization_.name));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Organization_.address));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildSpecification(criteria.getType(), Organization_.type));
            }
            if (criteria.getProjectsId() != null) {
                specification = specification.and(buildSpecification(criteria.getProjectsId(),
                    root -> root.join(Organization_.projects, JoinType.LEFT).get(Project_.id)));
            }
            if (criteria.getUsersId() != null) {
                specification = specification.and(buildSpecification(criteria.getUsersId(),
                    root -> root.join(Organization_.users, JoinType.LEFT).get(HDUser_.id)));
            }
        }
        return specification;
    }
}
