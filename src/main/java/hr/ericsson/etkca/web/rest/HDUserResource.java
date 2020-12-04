package hr.ericsson.etkca.web.rest;

import hr.ericsson.etkca.service.HDUserService;
import hr.ericsson.etkca.web.rest.errors.BadRequestAlertException;
import hr.ericsson.etkca.service.dto.HDUserDTO;
import hr.ericsson.etkca.service.dto.HDUserCriteria;
import hr.ericsson.etkca.service.HDUserQueryService;

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
 * REST controller for managing {@link hr.ericsson.etkca.domain.HDUser}.
 */
@RestController
@RequestMapping("/api")
public class HDUserResource {

    private final Logger log = LoggerFactory.getLogger(HDUserResource.class);

    private static final String ENTITY_NAME = "hDUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HDUserService hDUserService;

    private final HDUserQueryService hDUserQueryService;

    public HDUserResource(HDUserService hDUserService, HDUserQueryService hDUserQueryService) {
        this.hDUserService = hDUserService;
        this.hDUserQueryService = hDUserQueryService;
    }

    /**
     * {@code POST  /hd-users} : Create a new hDUser.
     *
     * @param hDUserDTO the hDUserDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hDUserDTO, or with status {@code 400 (Bad Request)} if the hDUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/hd-users")
    public ResponseEntity<HDUserDTO> createHDUser(@RequestBody HDUserDTO hDUserDTO) throws URISyntaxException {
        log.debug("REST request to save HDUser : {}", hDUserDTO);
        if (hDUserDTO.getId() != null) {
            throw new BadRequestAlertException("A new hDUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HDUserDTO result = hDUserService.save(hDUserDTO);
        return ResponseEntity.created(new URI("/api/hd-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /hd-users} : Updates an existing hDUser.
     *
     * @param hDUserDTO the hDUserDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hDUserDTO,
     * or with status {@code 400 (Bad Request)} if the hDUserDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hDUserDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hd-users")
    public ResponseEntity<HDUserDTO> updateHDUser(@RequestBody HDUserDTO hDUserDTO) throws URISyntaxException {
        log.debug("REST request to update HDUser : {}", hDUserDTO);
        if (hDUserDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HDUserDTO result = hDUserService.save(hDUserDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hDUserDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /hd-users} : get all the hDUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hDUsers in body.
     */
    @GetMapping("/hd-users")
    public ResponseEntity<List<HDUserDTO>> getAllHDUsers(HDUserCriteria criteria) {
        log.debug("REST request to get HDUsers by criteria: {}", criteria);
        List<HDUserDTO> entityList = hDUserQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /hd-users/count} : count all the hDUsers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/hd-users/count")
    public ResponseEntity<Long> countHDUsers(HDUserCriteria criteria) {
        log.debug("REST request to count HDUsers by criteria: {}", criteria);
        return ResponseEntity.ok().body(hDUserQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hd-users/:id} : get the "id" hDUser.
     *
     * @param id the id of the hDUserDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hDUserDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hd-users/{id}")
    public ResponseEntity<HDUserDTO> getHDUser(@PathVariable Long id) {
        log.debug("REST request to get HDUser : {}", id);
        Optional<HDUserDTO> hDUserDTO = hDUserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hDUserDTO);
    }

    /**
     * {@code DELETE  /hd-users/:id} : delete the "id" hDUser.
     *
     * @param id the id of the hDUserDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/hd-users/{id}")
    public ResponseEntity<Void> deleteHDUser(@PathVariable Long id) {
        log.debug("REST request to delete HDUser : {}", id);
        hDUserService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/hd-users?query=:query} : search for the hDUser corresponding
     * to the query.
     *
     * @param query the query of the hDUser search.
     * @return the result of the search.
     */
    @GetMapping("/_search/hd-users")
    public List<HDUserDTO> searchHDUsers(@RequestParam String query) {
        log.debug("REST request to search HDUsers for query {}", query);
        return hDUserService.search(query);
    }
}
