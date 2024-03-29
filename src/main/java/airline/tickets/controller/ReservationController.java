package airline.tickets.controller;

import airline.tickets.aspect.AspectAnnotation;
import airline.tickets.dto.ReservationDTO;
import airline.tickets.service.ReservationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Tag(name = "ReservationController")
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
    @AspectAnnotation
    public List<ReservationDTO> findByPassengerId(@PathVariable("passenger_id") final Long passengerId) {
        return reservationService.findByPassengerId(passengerId);
    }

    @GetMapping("/ticket_id/{ticket_id}")
    @AspectAnnotation
    public Optional<ReservationDTO> findByTicketId(@PathVariable("ticket_id") final Long ticketId) {
        return reservationService.findByTicketId(ticketId);
    }

    @PostMapping("/booking/ticket_id/{ticket_id}/passenger_id/{passenger_id}")
    @AspectAnnotation
    public ReservationDTO bookTicket(@PathVariable("ticket_id") final Long ticketId,
                                     @PathVariable("passenger_id") final Long passengerId) {
        return reservationService.saveReservation(passengerId, ticketId);
    }

    @DeleteMapping("/cancel_booking/ticket_id/{ticket_id}")
    @AspectAnnotation
    public void cancelTicketBooking(@PathVariable("ticket_id") final Long ticketId) {
        Optional<ReservationDTO> optionalReservation = reservationService.findByTicketId(ticketId);
        if (optionalReservation.isPresent()) {
            ReservationDTO reservationDTO = optionalReservation.get();
            reservationService.deleteReservation(reservationDTO.getId());
        }
    }
}
