package hr.ericsson.etkca.repository.search;

import hr.ericsson.etkca.domain.Organization;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Organization} entity.
 */
public interface OrganizationSearchRepository extends ElasticsearchRepository<Organization, Long> {
}
