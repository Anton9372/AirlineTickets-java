package airline.tickets.controller;

import airline.tickets.dto.FlightDTO;
import airline.tickets.dto.PassengerDTO;
import airline.tickets.dto.TicketDTO;
import airline.tickets.model.Ticket;
import airline.tickets.service.FlightService;
import airline.tickets.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
@AllArgsConstructor
public class FlightController {

    private final FlightService flightService;

    private final TicketService ticketService;

    @GetMapping
    public List<FlightDTO> findAllFlights() {
        return flightService.findAllFlights();
    }

    @GetMapping("/departure_town/{departure_town}")
    public List<FlightDTO> findByDepartureTown(@PathVariable("departure_town") String departureTown) {
        return flightService.findByDepartureTown(departureTown);
    }

    @GetMapping("/arrival_town/{arrival_town}")
    public List<FlightDTO> findByArrivalTown(@PathVariable("arrival_town") String arrivalTown) {
        return flightService.findByArrivalTown(arrivalTown);
    }

    @GetMapping("/departure_town/{departure_town}/arrival_town/{arrival_town}")
    public List<FlightDTO> findByDepartureTownAndArrivalTown(@PathVariable("departure_town") String departureTown,
                                                             @PathVariable("arrival_town") String arrivalTown) {
        return flightService.findByDepartureTownAndArrivalTown(departureTown, arrivalTown);
    }

    @GetMapping("/{flight_id}/tickets")
    public List<TicketDTO> findAllTickets(@PathVariable("flight_id") Long flightId) {
        return flightService.findAllTickets(flightId);
    }

    @GetMapping("/{flight_id}/passengers")
    public List<PassengerDTO> findAllPassengers(@PathVariable("flight_id") Long flightId) {
        return flightService.findAllPassengers(flightId);
    }

    @PostMapping("/save_tickets/{flight_id}/{num}")
    public List<TicketDTO> saveTickets(@RequestBody Ticket ticket, @PathVariable("flight_id") Long flightId,
                                       @PathVariable("num") int numOfTickets) {
        return ticketService.saveNumOfTickets(ticket, flightId, numOfTickets);
    }
}
