package hr.ericsson.etkca.web.rest;

import hr.ericsson.etkca.service.TicketService;
import hr.ericsson.etkca.web.rest.errors.BadRequestAlertException;
import hr.ericsson.etkca.service.dto.TicketDTO;
import hr.ericsson.etkca.service.dto.TicketCriteria;
import hr.ericsson.etkca.service.TicketQueryService;

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
 * REST controller for managing {@link hr.ericsson.etkca.domain.Ticket}.
 */
@RestController
@RequestMapping("/api")
public class TicketResource {

    private final Logger log = LoggerFactory.getLogger(TicketResource.class);

    private static final String ENTITY_NAME = "ticket";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TicketService ticketService;

    private final TicketQueryService ticketQueryService;

    public TicketResource(TicketService ticketService, TicketQueryService ticketQueryService) {
        this.ticketService = ticketService;
        this.ticketQueryService = ticketQueryService;
    }

    /**
     * {@code POST  /tickets} : Create a new ticket.
     *
     * @param ticketDTO the ticketDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ticketDTO, or with status {@code 400 (Bad Request)} if the ticket has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tickets")
    public ResponseEntity<TicketDTO> createTicket(@RequestBody TicketDTO ticketDTO) throws URISyntaxException {
        log.debug("REST request to save Ticket : {}", ticketDTO);
        if (ticketDTO.getId() != null) {
            throw new BadRequestAlertException("A new ticket cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TicketDTO result = ticketService.save(ticketDTO);
        return ResponseEntity.created(new URI("/api/tickets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tickets} : Updates an existing ticket.
     *
     * @param ticketDTO the ticketDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ticketDTO,
     * or with status {@code 400 (Bad Request)} if the ticketDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ticketDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tickets")
    public ResponseEntity<TicketDTO> updateTicket(@RequestBody TicketDTO ticketDTO) throws URISyntaxException {
        log.debug("REST request to update Ticket : {}", ticketDTO);
        if (ticketDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TicketDTO result = ticketService.save(ticketDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ticketDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /tickets} : get all the tickets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tickets in body.
     */
    @GetMapping("/tickets")
    public ResponseEntity<List<TicketDTO>> getAllTickets(TicketCriteria criteria) {
        log.debug("REST request to get Tickets by criteria: {}", criteria);
        List<TicketDTO> entityList = ticketQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /tickets/count} : count all the tickets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tickets/count")
    public ResponseEntity<Long> countTickets(TicketCriteria criteria) {
        log.debug("REST request to count Tickets by criteria: {}", criteria);
        return ResponseEntity.ok().body(ticketQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tickets/:id} : get the "id" ticket.
     *
     * @param id the id of the ticketDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ticketDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tickets/{id}")
    public ResponseEntity<TicketDTO> getTicket(@PathVariable Long id) {
        log.debug("REST request to get Ticket : {}", id);
        Optional<TicketDTO> ticketDTO = ticketService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ticketDTO);
    }

    /**
     * {@code DELETE  /tickets/:id} : delete the "id" ticket.
     *
     * @param id the id of the ticketDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tickets/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        log.debug("REST request to delete Ticket : {}", id);
        ticketService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/tickets?query=:query} : search for the ticket corresponding
     * to the query.
     *
     * @param query the query of the ticket search.
     * @return the result of the search.
     */
    @GetMapping("/_search/tickets")
    public List<TicketDTO> searchTickets(@RequestParam String query) {
        log.debug("REST request to search Tickets for query {}", query);
        return ticketService.search(query);
    }
}
