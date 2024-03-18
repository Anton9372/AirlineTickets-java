package airline.tickets.controller;

import airline.tickets.dto.TicketDTO;
import airline.tickets.model.Ticket;
import airline.tickets.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<TicketDTO> findByFlightId(@PathVariable("flight_id") Long flightId) {
        return ticketService.findByFlightId(flightId);
    }

    @GetMapping("/departure_town/{departure_town}")
    public List<TicketDTO> findByDepartureTown(@PathVariable("departure_town") String departureTown) {
        return ticketService.findByDepartureTown(departureTown);
    }

    @GetMapping("/arrival_town/{arrival_town}")
    public List<TicketDTO> findByArrivalTown(@PathVariable("arrival_town") String arrivalTown) {
        return ticketService.findByArrivalTown(arrivalTown);
    }

    @GetMapping("/departure_town/{departure_town}/arrival_town/{arrival_town}")
    public List<TicketDTO> findByDepartureTownAndArrivalTown(@PathVariable("departure_town") String departureTown,
                                                             @PathVariable("arrival_town") String arrivalTown) {
        return ticketService.findByDepartureTownAndArrivalTown(departureTown, arrivalTown);
    }

    @GetMapping("/unreserved/departure_town/{departure_town}")
    public List<TicketDTO> findUnreservedByDepartureTown(@PathVariable("departure_town") String departureTown) {
        List<TicketDTO> ticketDTOList = ticketService.findByDepartureTown(departureTown);
        return ticketService.findUnreserved(ticketDTOList);
    }

    @GetMapping("/unreserved/arrival_town/{arrival_town}")
    public List<TicketDTO> findUnreservedByArrivalTown(@PathVariable("arrival_town") String arrivalTown) {
        List<TicketDTO> ticketDTOList = ticketService.findByArrivalTown(arrivalTown);
        return ticketService.findUnreserved(ticketDTOList);
    }

    @GetMapping("/unreserved/departure_town/{departure_town}/arrival_town/{arrival_town}")
    public List<TicketDTO> findUnreservedByDepartureTownAndArrivalTown(
            @PathVariable("departure_town") String departureTown,
            @PathVariable("arrival_town") String arrivalTown) {
        List<TicketDTO> ticketDTOList = ticketService.findByDepartureTownAndArrivalTown(departureTown, arrivalTown);
        return ticketService.findUnreserved(ticketDTOList);
    }

    @PutMapping("/update_ticket/flight_id/{flight_id}")
    public TicketDTO updateTicket(@RequestBody Ticket ticket, @PathVariable("flight_id") Long flightId) {
        return ticketService.saveOrUpdateTicket(ticket, flightId);
    }

    @DeleteMapping("/delete_ticket/{ticket_id}")
    public void deleteTicket(@PathVariable("ticket_id") Long ticketId) {
        ticketService.deleteTicket(ticketId);
    }
}
