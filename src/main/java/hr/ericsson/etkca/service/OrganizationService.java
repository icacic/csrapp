package hr.ericsson.etkca.service;

import hr.ericsson.etkca.domain.Organization;
import hr.ericsson.etkca.repository.OrganizationRepository;
import hr.ericsson.etkca.repository.search.OrganizationSearchRepository;
import hr.ericsson.etkca.service.dto.OrganizationDTO;
import hr.ericsson.etkca.service.mapper.OrganizationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Organization}.
 */
@Service
@Transactional
public class OrganizationService {

    private final Logger log = LoggerFactory.getLogger(OrganizationService.class);

    private final OrganizationRepository organizationRepository;

    private final OrganizationMapper organizationMapper;

    private final OrganizationSearchRepository organizationSearchRepository;

    public OrganizationService(OrganizationRepository organizationRepository, OrganizationMapper organizationMapper, OrganizationSearchRepository organizationSearchRepository) {
        this.organizationRepository = organizationRepository;
        this.organizationMapper = organizationMapper;
        this.organizationSearchRepository = organizationSearchRepository;
    }

    /**
     * Save a organization.
     *
     * @param organizationDTO the entity to save.
     * @return the persisted entity.
     */
    public OrganizationDTO save(OrganizationDTO organizationDTO) {
        log.debug("Request to save Organization : {}", organizationDTO);
        Organization organization = organizationMapper.toEntity(organizationDTO);
        organization = organizationRepository.save(organization);
        OrganizationDTO result = organizationMapper.toDto(organization);
        organizationSearchRepository.save(organization);
        return result;
    }

    /**
     * Get all the organizations.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrganizationDTO> findAll() {
        log.debug("Request to get all Organizations");
        return organizationRepository.findAll().stream()
            .map(organizationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one organization by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrganizationDTO> findOne(Long id) {
        log.debug("Request to get Organization : {}", id);
        return organizationRepository.findById(id)
            .map(organizationMapper::toDto);
    }

    /**
     * Delete the organization by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Organization : {}", id);
        organizationRepository.deleteById(id);
        organizationSearchRepository.deleteById(id);
    }

    /**
     * Search for the organization corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrganizationDTO> search(String query) {
        log.debug("Request to search Organizations for query {}", query);
        return StreamSupport
            .stream(organizationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(organizationMapper::toDto)
        .collect(Collectors.toList());
    }
}
