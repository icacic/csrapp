package hr.ericsson.etkca.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link TicketStatusSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class TicketStatusSearchRepositoryMockConfiguration {

    @MockBean
    private TicketStatusSearchRepository mockTicketStatusSearchRepository;

}
