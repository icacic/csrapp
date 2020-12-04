package hr.ericsson.etkca.repository.search;

import hr.ericsson.etkca.domain.HDUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link HDUser} entity.
 */
public interface HDUserSearchRepository extends ElasticsearchRepository<HDUser, Long> {
}
