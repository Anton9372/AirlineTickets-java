package airline.tickets.controller;

import airline.tickets.aspect.LoggingAnnotation;
import airline.tickets.dto.TicketDTO;
import airline.tickets.model.Ticket;
import airline.tickets.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "TicketController")
@RestController
@RequestMapping("/api/v1/tickets")
@AllArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @Operation(summary = "Просмотр всех билетов")
    @GetMapping
    public List<TicketDTO> findAllTickets() {
        return ticketService.findAllTickets();
    }

    @Operation(summary = "Просмотр всех билетов рейса")
    @GetMapping("/flight_id/{flight_id}")
    @LoggingAnnotation
    public List<TicketDTO> findTicketsByFlightId(@PathVariable("flight_id") final Long flightId) {
        return ticketService.findAllTicketsByFlightId(flightId);
    }

    @Operation(summary = "Просмотр всех билетов по городу отправления")
    @GetMapping("/departure_town/{departure_town}")
    public List<TicketDTO> findTicketsByDepartureTown(@PathVariable("departure_town") final String departureTown) {
        return ticketService.findAllTicketsByDepartureTown(departureTown);
    }

    @Operation(summary = "Просмотр всех билетов по городу прибытия")
    @GetMapping("/arrival_town/{arrival_town}")
    public List<TicketDTO> findTicketsByArrivalTown(@PathVariable("arrival_town") final String arrivalTown) {
        return ticketService.findAllTicketsByArrivalTown(arrivalTown);
    }

    @Operation(summary = "Просмотр всех билетов по городу отправления и городу прибытия")
    @GetMapping("/departure_town/{departure_town}/arrival_town/{arrival_town}")
    public List<TicketDTO> findTicketsByDepartureTownAndArrivalTown(
            @PathVariable("departure_town") final String departureTown,
            @PathVariable("arrival_town") final String arrivalTown) {
        return ticketService.findAllTicketsByDepartureTownAndArrivalTown(departureTown, arrivalTown);
    }

    @Operation(summary = "Просмотр всех незабронированных билетов рейса")
    @GetMapping("/unreserved/flight_id/{flight_id}")
    public List<TicketDTO> findUnreservedTicketsByFlightId(@PathVariable("flight_id") final Long flightId) {
        return ticketService.findUnreservedTicketsByFlightId(flightId);
    }

    @Operation(summary = "Просмотр всех незабронированных билетов по городу отправления")
    @GetMapping("/unreserved/departure_town/{departure_town}")
    public List<TicketDTO> findUnreservedTicketsByDepartureTown(
            @PathVariable("departure_town") final String departureTown) {
        return ticketService.findUnreservedTicketsByDepartureTown(departureTown);
    }

    @Operation(summary = "Просмотр всех незабронированных билетов по городу прибытия")
    @GetMapping("/unreserved/arrival_town/{arrival_town}")
    public List<TicketDTO> findUnreservedTicketsByArrivalTown(@PathVariable("arrival_town") final String arrivalTown) {
        return ticketService.findUnreservedTicketsByArrivalTown(arrivalTown);
    }

    @Operation(summary = "Просмотр всех незабронированных билетов по городу отправления и городу прибытия")
    @GetMapping("/unreserved/departure_town/{departure_town}/arrival_town/{arrival_town}")
    public List<TicketDTO> findUnreservedTicketsByDepartureTownAndArrivalTown(
            @PathVariable("departure_town") final String departureTown,
            @PathVariable("arrival_town") final String arrivalTown) {
        return ticketService.findUnreservedTicketsByDepartureTownAndArrivalTown(departureTown, arrivalTown);
    }

    @Operation(summary = "Обновить билет")
    @PutMapping("/update_ticket/flight_id/{flight_id}")
    @LoggingAnnotation
    public TicketDTO updateTicket(@Valid @RequestBody final Ticket ticket,
                                  @PathVariable("flight_id") final Long flightId) {
        return ticketService.saveOrUpdateTicket(ticket, flightId);
    }

    @Operation(summary = "Удалить билет")
    @DeleteMapping("/delete_ticket/{ticket_id}")
    @LoggingAnnotation
    public void deleteTicket(@PathVariable("ticket_id") final Long ticketId) {
        ticketService.deleteTicket(ticketId);
    }
}
