package hr.ericsson.etkca.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import hr.ericsson.etkca.web.rest.TestUtil;

public class OrganizationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Organization.class);
        Organization organization1 = new Organization();
        organization1.setId(1L);
        Organization organization2 = new Organization();
        organization2.setId(organization1.getId());
        assertThat(organization1).isEqualTo(organization2);
        organization2.setId(2L);
        assertThat(organization1).isNotEqualTo(organization2);
        organization1.setId(null);
        assertThat(organization1).isNotEqualTo(organization2);
    }
}
