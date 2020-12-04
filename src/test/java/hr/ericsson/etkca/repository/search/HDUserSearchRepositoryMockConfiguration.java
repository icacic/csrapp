package hr.ericsson.etkca.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link HDUserSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class HDUserSearchRepositoryMockConfiguration {

    @MockBean
    private HDUserSearchRepository mockHDUserSearchRepository;

}
