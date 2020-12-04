package hr.ericsson.etkca.repository;

import hr.ericsson.etkca.domain.Ticket;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Ticket entity.
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>, JpaSpecificationExecutor<Ticket> {

    @Query(value = "select distinct ticket from Ticket ticket left join fetch ticket.users",
        countQuery = "select count(distinct ticket) from Ticket ticket")
    Page<Ticket> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct ticket from Ticket ticket left join fetch ticket.users")
    List<Ticket> findAllWithEagerRelationships();

    @Query("select ticket from Ticket ticket left join fetch ticket.users where ticket.id =:id")
    Optional<Ticket> findOneWithEagerRelationships(@Param("id") Long id);
}
