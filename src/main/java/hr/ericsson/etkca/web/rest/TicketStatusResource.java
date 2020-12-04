package hr.ericsson.etkca.web.rest;

import hr.ericsson.etkca.service.TicketStatusService;
import hr.ericsson.etkca.web.rest.errors.BadRequestAlertException;
import hr.ericsson.etkca.service.dto.TicketStatusDTO;
import hr.ericsson.etkca.service.dto.TicketStatusCriteria;
import hr.ericsson.etkca.service.TicketStatusQueryService;

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
 * REST controller for managing {@link hr.ericsson.etkca.domain.TicketStatus}.
 */
@RestController
@RequestMapping("/api")
public class TicketStatusResource {

    private final Logger log = LoggerFactory.getLogger(TicketStatusResource.class);

    private static final String ENTITY_NAME = "ticketStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TicketStatusService ticketStatusService;

    private final TicketStatusQueryService ticketStatusQueryService;

    public TicketStatusResource(TicketStatusService ticketStatusService, TicketStatusQueryService ticketStatusQueryService) {
        this.ticketStatusService = ticketStatusService;
        this.ticketStatusQueryService = ticketStatusQueryService;
    }

    /**
     * {@code POST  /ticket-statuses} : Create a new ticketStatus.
     *
     * @param ticketStatusDTO the ticketStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ticketStatusDTO, or with status {@code 400 (Bad Request)} if the ticketStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ticket-statuses")
    public ResponseEntity<TicketStatusDTO> createTicketStatus(@RequestBody TicketStatusDTO ticketStatusDTO) throws URISyntaxException {
        log.debug("REST request to save TicketStatus : {}", ticketStatusDTO);
        if (ticketStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new ticketStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TicketStatusDTO result = ticketStatusService.save(ticketStatusDTO);
        return ResponseEntity.created(new URI("/api/ticket-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ticket-statuses} : Updates an existing ticketStatus.
     *
     * @param ticketStatusDTO the ticketStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ticketStatusDTO,
     * or with status {@code 400 (Bad Request)} if the ticketStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ticketStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ticket-statuses")
    public ResponseEntity<TicketStatusDTO> updateTicketStatus(@RequestBody TicketStatusDTO ticketStatusDTO) throws URISyntaxException {
        log.debug("REST request to update TicketStatus : {}", ticketStatusDTO);
        if (ticketStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TicketStatusDTO result = ticketStatusService.save(ticketStatusDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ticketStatusDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ticket-statuses} : get all the ticketStatuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ticketStatuses in body.
     */
    @GetMapping("/ticket-statuses")
    public ResponseEntity<List<TicketStatusDTO>> getAllTicketStatuses(TicketStatusCriteria criteria) {
        log.debug("REST request to get TicketStatuses by criteria: {}", criteria);
        List<TicketStatusDTO> entityList = ticketStatusQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /ticket-statuses/count} : count all the ticketStatuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ticket-statuses/count")
    public ResponseEntity<Long> countTicketStatuses(TicketStatusCriteria criteria) {
        log.debug("REST request to count TicketStatuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(ticketStatusQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ticket-statuses/:id} : get the "id" ticketStatus.
     *
     * @param id the id of the ticketStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ticketStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ticket-statuses/{id}")
    public ResponseEntity<TicketStatusDTO> getTicketStatus(@PathVariable Long id) {
        log.debug("REST request to get TicketStatus : {}", id);
        Optional<TicketStatusDTO> ticketStatusDTO = ticketStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ticketStatusDTO);
    }

    /**
     * {@code DELETE  /ticket-statuses/:id} : delete the "id" ticketStatus.
     *
     * @param id the id of the ticketStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ticket-statuses/{id}")
    public ResponseEntity<Void> deleteTicketStatus(@PathVariable Long id) {
        log.debug("REST request to delete TicketStatus : {}", id);
        ticketStatusService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/ticket-statuses?query=:query} : search for the ticketStatus corresponding
     * to the query.
     *
     * @param query the query of the ticketStatus search.
     * @return the result of the search.
     */
    @GetMapping("/_search/ticket-statuses")
    public List<TicketStatusDTO> searchTicketStatuses(@RequestParam String query) {
        log.debug("REST request to search TicketStatuses for query {}", query);
        return ticketStatusService.search(query);
    }
}
