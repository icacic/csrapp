package hr.ericsson.etkca.service;

import hr.ericsson.etkca.domain.TicketStatus;
import hr.ericsson.etkca.repository.TicketStatusRepository;
import hr.ericsson.etkca.repository.search.TicketStatusSearchRepository;
import hr.ericsson.etkca.service.dto.TicketStatusDTO;
import hr.ericsson.etkca.service.mapper.TicketStatusMapper;
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
 * Service Implementation for managing {@link TicketStatus}.
 */
@Service
@Transactional
public class TicketStatusService {

    private final Logger log = LoggerFactory.getLogger(TicketStatusService.class);

    private final TicketStatusRepository ticketStatusRepository;

    private final TicketStatusMapper ticketStatusMapper;

    private final TicketStatusSearchRepository ticketStatusSearchRepository;

    public TicketStatusService(TicketStatusRepository ticketStatusRepository, TicketStatusMapper ticketStatusMapper, TicketStatusSearchRepository ticketStatusSearchRepository) {
        this.ticketStatusRepository = ticketStatusRepository;
        this.ticketStatusMapper = ticketStatusMapper;
        this.ticketStatusSearchRepository = ticketStatusSearchRepository;
    }

    /**
     * Save a ticketStatus.
     *
     * @param ticketStatusDTO the entity to save.
     * @return the persisted entity.
     */
    public TicketStatusDTO save(TicketStatusDTO ticketStatusDTO) {
        log.debug("Request to save TicketStatus : {}", ticketStatusDTO);
        TicketStatus ticketStatus = ticketStatusMapper.toEntity(ticketStatusDTO);
        ticketStatus = ticketStatusRepository.save(ticketStatus);
        TicketStatusDTO result = ticketStatusMapper.toDto(ticketStatus);
        ticketStatusSearchRepository.save(ticketStatus);
        return result;
    }

    /**
     * Get all the ticketStatuses.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TicketStatusDTO> findAll() {
        log.debug("Request to get all TicketStatuses");
        return ticketStatusRepository.findAll().stream()
            .map(ticketStatusMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one ticketStatus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TicketStatusDTO> findOne(Long id) {
        log.debug("Request to get TicketStatus : {}", id);
        return ticketStatusRepository.findById(id)
            .map(ticketStatusMapper::toDto);
    }

    /**
     * Delete the ticketStatus by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TicketStatus : {}", id);
        ticketStatusRepository.deleteById(id);
        ticketStatusSearchRepository.deleteById(id);
    }

    /**
     * Search for the ticketStatus corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TicketStatusDTO> search(String query) {
        log.debug("Request to search TicketStatuses for query {}", query);
        return StreamSupport
            .stream(ticketStatusSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(ticketStatusMapper::toDto)
        .collect(Collectors.toList());
    }
}
