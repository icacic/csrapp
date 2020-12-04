package hr.ericsson.etkca.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import hr.ericsson.etkca.web.rest.TestUtil;

public class PriorityTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Priority.class);
        Priority priority1 = new Priority();
        priority1.setId(1L);
        Priority priority2 = new Priority();
        priority2.setId(priority1.getId());
        assertThat(priority1).isEqualTo(priority2);
        priority2.setId(2L);
        assertThat(priority1).isNotEqualTo(priority2);
        priority1.setId(null);
        assertThat(priority1).isNotEqualTo(priority2);
    }
}
