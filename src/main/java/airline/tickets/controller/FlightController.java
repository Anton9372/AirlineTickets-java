package airline.tickets.controller;

import airline.tickets.aspect.AspectAnnotation;
import airline.tickets.dto.FlightDTO;
import airline.tickets.dto.PassengerDTO;
import airline.tickets.dto.TicketDTO;
import airline.tickets.model.Ticket;
import airline.tickets.service.FlightService;
import airline.tickets.service.TicketService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "FlightController")
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
    public List<FlightDTO> findByDepartureTown(@PathVariable("departure_town") final String departureTown) {
        return flightService.findByDepartureTown(departureTown);
    }

    @GetMapping("/arrival_town/{arrival_town}")
    public List<FlightDTO> findByArrivalTown(@PathVariable("arrival_town") final String arrivalTown) {
        return flightService.findByArrivalTown(arrivalTown);
    }

    @GetMapping("/departure_town/{departure_town}/arrival_town/{arrival_town}")
    public List<FlightDTO> findByDepartureTownAndArrivalTown(@PathVariable("departure_town") final String departureTown,
                                                             @PathVariable("arrival_town") final String arrivalTown) {
        return flightService.findByDepartureTownAndArrivalTown(departureTown, arrivalTown);
    }

    @GetMapping("/{flight_id}/tickets")
    @AspectAnnotation
    public List<TicketDTO> findAllTickets(@PathVariable("flight_id") final Long flightId) {
        return flightService.findAllTickets(flightId);
    }

    @GetMapping("/{flight_id}/passengers")
    @AspectAnnotation
    public List<PassengerDTO> findAllPassengers(@PathVariable("flight_id") final Long flightId) {
        return flightService.findAllPassengers(flightId);
    }

    @PostMapping("/save_tickets/{flight_id}/{num}")
    @AspectAnnotation
    public List<TicketDTO> saveTickets(@Valid @RequestBody final Ticket ticket,
                                       @PathVariable("flight_id") final Long flightId,
                                       @PathVariable("num") final int numOfTickets) {
        return ticketService.saveNumOfTickets(ticket, flightId, numOfTickets);
    }
}
