package hr.ericsson.etkca.repository.search;

import hr.ericsson.etkca.domain.Category;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Category} entity.
 */
public interface CategorySearchRepository extends ElasticsearchRepository<Category, Long> {
}
