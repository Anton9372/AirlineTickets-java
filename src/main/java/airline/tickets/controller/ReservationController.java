package airline.tickets.controller;

import airline.tickets.dto.ReservationDTO;
import airline.tickets.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/reservations")
@AllArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public List<ReservationDTO> findAllReservations() {
        return reservationService.findAllReservations();
    }

    @GetMapping("/passenger_id/{passenger_id}")
    public List<ReservationDTO> findByPassengerId(@PathVariable("passenger_id") Long passengerId) {
        return reservationService.findByPassengerId(passengerId);
    }

    @GetMapping("/ticket_id/{ticket_id}")
    public ResponseEntity<ReservationDTO> findByTicketId(@PathVariable("ticket_id") Long ticketId) {
        Optional<ReservationDTO> optionalReservationDTO = reservationService.findByTicketId(ticketId);
        return optionalReservationDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/booking/ticket_id/{ticket_id}/passenger_id/{passenger_id}")
    public ReservationDTO bookTicket(@PathVariable("ticket_id") Long ticketId,
                                     @PathVariable("passenger_id") Long passengerId) {
        return reservationService.saveReservation(passengerId, ticketId);
    }

    @DeleteMapping("/cancel_booking/ticket_id/{ticket_id}")
    public void cancelTicketBooking(@PathVariable("ticket_id") Long ticketId) {
        Optional<ReservationDTO> optionalReservation = reservationService.findByTicketId(ticketId);
        if (optionalReservation.isPresent()) {
            ReservationDTO reservationDTO = optionalReservation.get();
            reservationService.deleteReservation(reservationDTO.getId());
        }
    }
}
