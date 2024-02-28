package Big.AirlineTickets.repository;

import Big.AirlineTickets.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketsRepository extends JpaRepository<Ticket, Long> {
    Ticket findByDepartureTown(String departureTown);
    void deleteByDepartureTown(String departureTown);
}
