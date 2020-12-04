package hr.ericsson.etkca.web.rest;

import hr.ericsson.etkca.service.OrganizationService;
import hr.ericsson.etkca.web.rest.errors.BadRequestAlertException;
import hr.ericsson.etkca.service.dto.OrganizationDTO;
import hr.ericsson.etkca.service.dto.OrganizationCriteria;
import hr.ericsson.etkca.service.OrganizationQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link hr.ericsson.etkca.domain.Organization}.
 */
@RestController
@RequestMapping("/api")
public class OrganizationResource {

    private final Logger log = LoggerFactory.getLogger(OrganizationResource.class);

    private static final String ENTITY_NAME = "organization";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrganizationService organizationService;

    private final OrganizationQueryService organizationQueryService;

    public OrganizationResource(OrganizationService organizationService, OrganizationQueryService organizationQueryService) {
        this.organizationService = organizationService;
        this.organizationQueryService = organizationQueryService;
    }

    /**
     * {@code POST  /organizations} : Create a new organization.
     *
     * @param organizationDTO the organizationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new organizationDTO, or with status {@code 400 (Bad Request)} if the organization has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/organizations")
    public ResponseEntity<OrganizationDTO> createOrganization(@RequestBody OrganizationDTO organizationDTO) throws URISyntaxException {
        log.debug("REST request to save Organization : {}", organizationDTO);
        if (organizationDTO.getId() != null) {
            throw new BadRequestAlertException("A new organization cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrganizationDTO result = organizationService.save(organizationDTO);
        return ResponseEntity.created(new URI("/api/organizations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /organizations} : Updates an existing organization.
     *
     * @param organizationDTO the organizationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated organizationDTO,
     * or with status {@code 400 (Bad Request)} if the organizationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the organizationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/organizations")
    public ResponseEntity<OrganizationDTO> updateOrganization(@RequestBody OrganizationDTO organizationDTO) throws URISyntaxException {
        log.debug("REST request to update Organization : {}", organizationDTO);
        if (organizationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OrganizationDTO result = organizationService.save(organizationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, organizationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /organizations} : get all the organizations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of organizations in body.
     */
    @GetMapping("/organizations")
    public ResponseEntity<List<OrganizationDTO>> getAllOrganizations(OrganizationCriteria criteria) {
        log.debug("REST request to get Organizations by criteria: {}", criteria);
        List<OrganizationDTO> entityList = organizationQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /organizations/count} : count all the organizations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/organizations/count")
    public ResponseEntity<Long> countOrganizations(OrganizationCriteria criteria) {
        log.debug("REST request to count Organizations by criteria: {}", criteria);
        return ResponseEntity.ok().body(organizationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /organizations/:id} : get the "id" organization.
     *
     * @param id the id of the organizationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the organizationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/organizations/{id}")
    public ResponseEntity<OrganizationDTO> getOrganization(@PathVariable Long id) {
        log.debug("REST request to get Organization : {}", id);
        Optional<OrganizationDTO> organizationDTO = organizationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(organizationDTO);
    }

    /**
     * {@code DELETE  /organizations/:id} : delete the "id" organization.
     *
     * @param id the id of the organizationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/organizations/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable Long id) {
        log.debug("REST request to delete Organization : {}", id);
        organizationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/organizations?query=:query} : search for the organization corresponding
     * to the query.
     *
     * @param query the query of the organization search.
     * @return the result of the search.
     */
    @GetMapping("/_search/organizations")
    public List<OrganizationDTO> searchOrganizations(@RequestParam String query) {
        log.debug("REST request to search Organizations for query {}", query);
        return organizationService.search(query);
    }
}
