package hr.ericsson.etkca.repository;

import hr.ericsson.etkca.domain.TicketStatus;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TicketStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TicketStatusRepository extends JpaRepository<TicketStatus, Long>, JpaSpecificationExecutor<TicketStatus> {
}
