package airline.tickets.controller;

import airline.tickets.aspect.AspectAnnotation;
import airline.tickets.dto.TicketDTO;
import airline.tickets.model.Ticket;
import airline.tickets.service.TicketService;
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

    @GetMapping
    public List<TicketDTO> findAllTickets() {
        return ticketService.findAllTickets();
    }

    @GetMapping("/flight_id/{flight_id}")
    @AspectAnnotation
    public List<TicketDTO> findByFlightId(@PathVariable("flight_id") final Long flightId) {
        return ticketService.findByFlightId(flightId);
    }

    @GetMapping("/departure_town/{departure_town}")
    public List<TicketDTO> findByDepartureTown(@PathVariable("departure_town") final String departureTown) {
        return ticketService.findByDepartureTown(departureTown);
    }

    @GetMapping("/arrival_town/{arrival_town}")
    public List<TicketDTO> findByArrivalTown(@PathVariable("arrival_town") final String arrivalTown) {
        return ticketService.findByArrivalTown(arrivalTown);
    }

    @GetMapping("/departure_town/{departure_town}/arrival_town/{arrival_town}")
    public List<TicketDTO> findByDepartureTownAndArrivalTown(@PathVariable("departure_town") final String departureTown,
                                                             @PathVariable("arrival_town") final String arrivalTown) {
        return ticketService.findByDepartureTownAndArrivalTown(departureTown, arrivalTown);
    }

    @GetMapping("/unreserved/departure_town/{departure_town}")
    public List<TicketDTO> findUnreservedByDepartureTown(@PathVariable("departure_town") final String departureTown) {
        List<TicketDTO> ticketDTOList = ticketService.findByDepartureTown(departureTown);
        return ticketService.findUnreserved(ticketDTOList);
    }

    @GetMapping("/unreserved/arrival_town/{arrival_town}")
    public List<TicketDTO> findUnreservedByArrivalTown(@PathVariable("arrival_town") final String arrivalTown) {
        List<TicketDTO> ticketDTOList = ticketService.findByArrivalTown(arrivalTown);
        return ticketService.findUnreserved(ticketDTOList);
    }

    @GetMapping("/unreserved/departure_town/{departure_town}/arrival_town/{arrival_town}")
    public List<TicketDTO> findUnreservedByDepartureTownAndArrivalTown(
            @PathVariable("departure_town") final String departureTown,
            @PathVariable("arrival_town") final String arrivalTown) {
        List<TicketDTO> ticketDTOList = ticketService.findByDepartureTownAndArrivalTown(departureTown, arrivalTown);
        return ticketService.findUnreserved(ticketDTOList);
    }

    @PutMapping("/update_ticket/flight_id/{flight_id}")
    @AspectAnnotation
    public TicketDTO updateTicket(@Valid @RequestBody final Ticket ticket,
                                  @PathVariable("flight_id") final Long flightId) {
        return ticketService.saveOrUpdateTicket(ticket, flightId);
    }

    @DeleteMapping("/delete_ticket/{ticket_id}")
    @AspectAnnotation
    public void deleteTicket(@PathVariable("ticket_id") final Long ticketId) {
        ticketService.deleteTicket(ticketId);
    }
}
