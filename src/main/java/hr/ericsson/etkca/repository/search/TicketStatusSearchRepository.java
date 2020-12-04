package hr.ericsson.etkca.repository.search;

import hr.ericsson.etkca.domain.TicketStatus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link TicketStatus} entity.
 */
public interface TicketStatusSearchRepository extends ElasticsearchRepository<TicketStatus, Long> {
}
