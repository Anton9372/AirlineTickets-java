package airline.tickets.service;

import airline.tickets.dto.ReservationDTO;

import java.util.List;
import java.util.Optional;

public interface ReservationService {

    List<ReservationDTO> findAllReservations();

    List<ReservationDTO> findByPassengerId(Long passengerId);

    Optional<ReservationDTO> findByTicketId(Long ticketId);

    ReservationDTO saveReservation(Long passengerId, Long ticketId);

    void deleteReservation(Long reservationId);
}
