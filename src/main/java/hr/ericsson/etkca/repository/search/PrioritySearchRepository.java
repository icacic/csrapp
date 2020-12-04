package hr.ericsson.etkca.repository.search;

import hr.ericsson.etkca.domain.Priority;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Priority} entity.
 */
public interface PrioritySearchRepository extends ElasticsearchRepository<Priority, Long> {
}
