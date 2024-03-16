package airline.tickets.repository;

import airline.tickets.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByFlightId(Long flightId);
}
