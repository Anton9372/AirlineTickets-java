package airline.tickets.repository;

import airline.tickets.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @Query("SELECT t FROM Ticket t WHERE t.flight.id = :flightId")
    List<Ticket> findByFlightId(@Param("flightId") Long flightId);
}
