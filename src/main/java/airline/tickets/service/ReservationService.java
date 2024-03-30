package airline.tickets.service;

import airline.tickets.dto.ReservationDTO;
import airline.tickets.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface ReservationService {

    List<ReservationDTO> findAllReservations();

    List<ReservationDTO> findByPassengerId(Long passengerId) throws ResourceNotFoundException;

    Optional<ReservationDTO> findByTicketId(Long ticketId) throws ResourceNotFoundException;

    ReservationDTO saveReservation(Long passengerId, Long ticketId) throws ResourceNotFoundException;

    List<ReservationDTO> saveBulkReservations(Long passengerId, List<Long> ticketIds)
            throws ResourceNotFoundException;

    void deleteReservation(Long reservationId) throws ResourceNotFoundException;
}
