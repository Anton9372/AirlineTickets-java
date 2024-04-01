package airline.tickets.controller;

import airline.tickets.aspect.AspectAnnotation;
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
    @AspectAnnotation
    public List<TicketDTO> findTicketsByFlightId(@PathVariable("flight_id") final Long flightId) {
        return ticketService.findTicketsByFlightId(flightId);
    }

    @Operation(summary = "Просмотр всех билетов по городу отправления")
    @GetMapping("/departure_town/{departure_town}")
    public List<TicketDTO> findTicketsByDepartureTown(@PathVariable("departure_town") final String departureTown) {
        return ticketService.findTicketsByDepartureTown(departureTown);
    }

    @Operation(summary = "Просмотр всех билетов по городу прибытия")
    @GetMapping("/arrival_town/{arrival_town}")
    public List<TicketDTO> findTicketsByArrivalTown(@PathVariable("arrival_town") final String arrivalTown) {
        return ticketService.findTicketsByArrivalTown(arrivalTown);
    }

    @Operation(summary = "Просмотр всех билетов по городу отправления и городу прибытия")
    @GetMapping("/departure_town/{departure_town}/arrival_town/{arrival_town}")
    public List<TicketDTO> findTicketsByDepartureTownAndArrivalTown(
            @PathVariable("departure_town") final String departureTown,
            @PathVariable("arrival_town") final String arrivalTown) {
        return ticketService.findTicketsByDepartureTownAndArrivalTown(departureTown, arrivalTown);
    }

    @Operation(summary = "Просмотр всех незабронированных билетов по городу отправления")
    @GetMapping("/unreserved/departure_town/{departure_town}")
    public List<TicketDTO> findUnreservedTicketsByDepartureTown(
            @PathVariable("departure_town") final String departureTown) {
        List<TicketDTO> ticketDTOList = ticketService.findTicketsByDepartureTown(departureTown);
        return ticketService.findUnreservedTicketsFromList(ticketDTOList);
    }

    @Operation(summary = "Просмотр всех незабронированных билетов по городу прибытия")
    @GetMapping("/unreserved/arrival_town/{arrival_town}")
    public List<TicketDTO> findUnreservedByArrivalTown(@PathVariable("arrival_town") final String arrivalTown) {
        List<TicketDTO> ticketDTOList = ticketService.findTicketsByArrivalTown(arrivalTown);
        return ticketService.findUnreservedTicketsFromList(ticketDTOList);
    }

    @Operation(summary = "Просмотр всех незабронированных билетов по городу отправления и городу прибытия")
    @GetMapping("/unreserved/departure_town/{departure_town}/arrival_town/{arrival_town}")
    public List<TicketDTO> findUnreservedByDepartureTownAndArrivalTown(
            @PathVariable("departure_town") final String departureTown,
            @PathVariable("arrival_town") final String arrivalTown) {
        List<TicketDTO> ticketDTOList = ticketService.
                findTicketsByDepartureTownAndArrivalTown(departureTown, arrivalTown);
        return ticketService.findUnreservedTicketsFromList(ticketDTOList);
    }

    @Operation(summary = "Обновить билет")
    @PutMapping("/update_ticket/flight_id/{flight_id}")
    @AspectAnnotation
    public TicketDTO updateTicket(@Valid @RequestBody final Ticket ticket,
                                  @PathVariable("flight_id") final Long flightId) {
        return ticketService.saveOrUpdateTicket(ticket, flightId);
    }

    @Operation(summary = "Удалить билет")
    @DeleteMapping("/delete_ticket/{ticket_id}")
    @AspectAnnotation
    public void deleteTicket(@PathVariable("ticket_id") final Long ticketId) {
        ticketService.deleteTicket(ticketId);
    }
}
