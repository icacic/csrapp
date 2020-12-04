package hr.ericsson.etkca.repository.search;

import hr.ericsson.etkca.domain.Ticket;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Ticket} entity.
 */
public interface TicketSearchRepository extends ElasticsearchRepository<Ticket, Long> {
}
