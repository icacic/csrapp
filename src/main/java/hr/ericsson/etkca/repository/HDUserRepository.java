package hr.ericsson.etkca.repository;

import hr.ericsson.etkca.domain.HDUser;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the HDUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HDUserRepository extends JpaRepository<HDUser, Long>, JpaSpecificationExecutor<HDUser> {

    @Query("select hDUser from HDUser hDUser where hDUser.user.login = ?#{principal.username}")
    List<HDUser> findByUserIsCurrentUser();
}
