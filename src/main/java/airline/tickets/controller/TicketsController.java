package airline.tickets.controller;

import airline.tickets.service.TicketsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import airline.tickets.model.Ticket;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@AllArgsConstructor
public class TicketsController {

    private final TicketsService service;

    @GetMapping
    public List<Ticket> findAllTickets() {
        return service.findAllTickets();
    }

    @PostMapping("save_ticket")
    public Ticket saveTicket(@RequestBody Ticket ticket) {
        return service.saveTicket(ticket);
    }

    @GetMapping("/{departure_town}")
    public Ticket findByDepartureTown(@PathVariable("departure_town") String departureTown) {
        return service.findByDepartureTown(departureTown);
    }

    @PutMapping("update_ticket")
    public Ticket updateTicket(@RequestBody Ticket ticket) {
        return service.updateTicket(ticket);
    }

    @DeleteMapping("delete_ticket/{departure_town}")
    public void deleteByDepartureTown(@PathVariable("departure_town") String departureTown) {
        service.deleteByDepartureTown(departureTown);
    }
}
