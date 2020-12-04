package hr.ericsson.etkca.repository.search;

import hr.ericsson.etkca.domain.Attachment;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


/**
 * Spring Data Elasticsearch repository for the {@link Attachment} entity.
 */
public interface AttachmentSearchRepository extends ElasticsearchRepository<Attachment, Long> {
}
