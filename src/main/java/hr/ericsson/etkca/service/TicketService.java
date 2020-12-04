package hr.ericsson.etkca.service;

import hr.ericsson.etkca.domain.Ticket;
import hr.ericsson.etkca.repository.TicketRepository;
import hr.ericsson.etkca.repository.search.TicketSearchRepository;
import hr.ericsson.etkca.service.dto.TicketDTO;
import hr.ericsson.etkca.service.mapper.TicketMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Ticket}.
 */
@Service
@Transactional
public class TicketService {

    private final Logger log = LoggerFactory.getLogger(TicketService.class);

    private final TicketRepository ticketRepository;

    private final TicketMapper ticketMapper;

    private final TicketSearchRepository ticketSearchRepository;

    public TicketService(TicketRepository ticketRepository, TicketMapper ticketMapper, TicketSearchRepository ticketSearchRepository) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
        this.ticketSearchRepository = ticketSearchRepository;
    }

    /**
     * Save a ticket.
     *
     * @param ticketDTO the entity to save.
     * @return the persisted entity.
     */
    public TicketDTO save(TicketDTO ticketDTO) {
        log.debug("Request to save Ticket : {}", ticketDTO);
        Ticket ticket = ticketMapper.toEntity(ticketDTO);
        ticket = ticketRepository.save(ticket);
        TicketDTO result = ticketMapper.toDto(ticket);
        ticketSearchRepository.save(ticket);
        return result;
    }

    /**
     * Get all the tickets.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TicketDTO> findAll() {
        log.debug("Request to get all Tickets");
        return ticketRepository.findAllWithEagerRelationships().stream()
            .map(ticketMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get all the tickets with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TicketDTO> findAllWithEagerRelationships(Pageable pageable) {
        return ticketRepository.findAllWithEagerRelationships(pageable).map(ticketMapper::toDto);
    }

    /**
     * Get one ticket by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TicketDTO> findOne(Long id) {
        log.debug("Request to get Ticket : {}", id);
        return ticketRepository.findOneWithEagerRelationships(id)
            .map(ticketMapper::toDto);
    }

    /**
     * Delete the ticket by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Ticket : {}", id);
        ticketRepository.deleteById(id);
        ticketSearchRepository.deleteById(id);
    }

    /**
     * Search for the ticket corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TicketDTO> search(String query) {
        log.debug("Request to search Tickets for query {}", query);
        return StreamSupport
            .stream(ticketSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(ticketMapper::toDto)
        .collect(Collectors.toList());
    }
}
