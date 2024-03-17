package airline.tickets.repository;

import airline.tickets.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.passenger.id = :passengerId")
    List<Reservation> findByPassengerId(@Param("passengerId") Long passengerId);

    @Query("SELECT r FROM Reservation r WHERE r.ticket.id = :ticketId")
    Optional<Reservation> findByTicketId(@Param("ticketId") Long ticketId);
}
