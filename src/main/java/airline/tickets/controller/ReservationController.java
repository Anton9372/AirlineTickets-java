package airline.tickets.controller;

import airline.tickets.aspect.AspectAnnotation;
import airline.tickets.dto.ReservationDTO;
import airline.tickets.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @Operation(summary = "Просмотр всех бронирований")
    @GetMapping
    public List<ReservationDTO> findAllReservations() {
        return reservationService.findAllReservations();
    }

    @Operation(summary = "Просмотр всех бронирований пассажира")
    @GetMapping("/passenger_id/{passenger_id}")
    @AspectAnnotation
    public List<ReservationDTO> findReservationByPassengerId(@PathVariable("passenger_id") final Long passengerId) {
        return reservationService.findReservationsByPassengerId(passengerId);
    }

    @Operation(summary = "Просмотр бронирования по билету")
    @GetMapping("/ticket_id/{ticket_id}")
    @AspectAnnotation
    public Optional<ReservationDTO> findReservationByTicketId(@PathVariable("ticket_id") final Long ticketId) {
        return reservationService.findReservationByTicketId(ticketId);
    }

    @Operation(summary = "Забронировать билет")
    @PostMapping("/booking/ticket_id/{ticket_id}/passenger_id/{passenger_id}")
    @AspectAnnotation
    public ReservationDTO bookTicket(@PathVariable("ticket_id") final Long ticketId,
                                     @PathVariable("passenger_id") final Long passengerId) {
        return reservationService.saveReservation(passengerId, ticketId);
    }

    @Operation(summary = "Забронировать несколько билетов")
    @PostMapping("/booking/bulk/passenger_id/{passenger_id}")
    @AspectAnnotation
    public List<ReservationDTO> bookBulkTickets(@PathVariable("passenger_id") final Long passengerId,
                                                @RequestBody final List<Long> ticketIds) {
        return reservationService.saveBulkReservations(passengerId, ticketIds);
    }

    @Operation(summary = "Отменить бронирование билета")
    @DeleteMapping("/cancel_booking/ticket_id/{ticket_id}")
    @AspectAnnotation
    public void cancelTicketBooking(@PathVariable("ticket_id") final Long ticketId) {
        Optional<ReservationDTO> optionalReservation = reservationService.findReservationByTicketId(ticketId);
        if (optionalReservation.isPresent()) {
            ReservationDTO reservationDTO = optionalReservation.get();
            reservationService.deleteReservation(reservationDTO.getId());
        }
    }
}
