package hr.ericsson.etkca.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import hr.ericsson.etkca.web.rest.TestUtil;

public class HDUserTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HDUser.class);
        HDUser hDUser1 = new HDUser();
        hDUser1.setId(1L);
        HDUser hDUser2 = new HDUser();
        hDUser2.setId(hDUser1.getId());
        assertThat(hDUser1).isEqualTo(hDUser2);
        hDUser2.setId(2L);
        assertThat(hDUser1).isNotEqualTo(hDUser2);
        hDUser1.setId(null);
        assertThat(hDUser1).isNotEqualTo(hDUser2);
    }
}
