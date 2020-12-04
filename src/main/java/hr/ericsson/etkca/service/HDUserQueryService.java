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

import hr.ericsson.etkca.domain.HDUser;
import hr.ericsson.etkca.domain.*; // for static metamodels
import hr.ericsson.etkca.repository.HDUserRepository;
import hr.ericsson.etkca.repository.search.HDUserSearchRepository;
import hr.ericsson.etkca.service.dto.HDUserCriteria;
import hr.ericsson.etkca.service.dto.HDUserDTO;
import hr.ericsson.etkca.service.mapper.HDUserMapper;

/**
 * Service for executing complex queries for {@link HDUser} entities in the database.
 * The main input is a {@link HDUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HDUserDTO} or a {@link Page} of {@link HDUserDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HDUserQueryService extends QueryService<HDUser> {

    private final Logger log = LoggerFactory.getLogger(HDUserQueryService.class);

    private final HDUserRepository hDUserRepository;

    private final HDUserMapper hDUserMapper;

    private final HDUserSearchRepository hDUserSearchRepository;

    public HDUserQueryService(HDUserRepository hDUserRepository, HDUserMapper hDUserMapper, HDUserSearchRepository hDUserSearchRepository) {
        this.hDUserRepository = hDUserRepository;
        this.hDUserMapper = hDUserMapper;
        this.hDUserSearchRepository = hDUserSearchRepository;
    }

    /**
     * Return a {@link List} of {@link HDUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HDUserDTO> findByCriteria(HDUserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<HDUser> specification = createSpecification(criteria);
        return hDUserMapper.toDto(hDUserRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link HDUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HDUserDTO> findByCriteria(HDUserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HDUser> specification = createSpecification(criteria);
        return hDUserRepository.findAll(specification, page)
            .map(hDUserMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HDUserCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<HDUser> specification = createSpecification(criteria);
        return hDUserRepository.count(specification);
    }

    /**
     * Function to convert {@link HDUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HDUser> createSpecification(HDUserCriteria criteria) {
        Specification<HDUser> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HDUser_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), HDUser_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), HDUser_.lastName));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), HDUser_.email));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), HDUser_.address));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(HDUser_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getOrganizationId() != null) {
                specification = specification.and(buildSpecification(criteria.getOrganizationId(),
                    root -> root.join(HDUser_.organization, JoinType.LEFT).get(Organization_.id)));
            }
            if (criteria.getProjectsId() != null) {
                specification = specification.and(buildSpecification(criteria.getProjectsId(),
                    root -> root.join(HDUser_.projects, JoinType.LEFT).get(Project_.id)));
            }
            if (criteria.getTicketsId() != null) {
                specification = specification.and(buildSpecification(criteria.getTicketsId(),
                    root -> root.join(HDUser_.tickets, JoinType.LEFT).get(Ticket_.id)));
            }
        }
        return specification;
    }
}
