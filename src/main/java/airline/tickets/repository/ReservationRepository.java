package airline.tickets.repository;

import airline.tickets.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByPassengerId(Long passengerId);

    Optional<Reservation> findByTicketId(Long ticketId);
}
