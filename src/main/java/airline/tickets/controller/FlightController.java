package airline.tickets.controller;

import airline.tickets.service.FlightService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import airline.tickets.model.Flight;

import java.util.List;

@RestController
@RequestMapping("/api/v1/flights")
@AllArgsConstructor
public class FlightController {

    private final FlightService service;

    @GetMapping
    public List<Flight> findAllFlights() {
        return service.findAllFlights();
    }

    @PostMapping("save_flight")
    public Flight saveFlight(@RequestBody Flight flight) {
        return service.saveFlight(flight);
    }

    @GetMapping("/{departure_town}")
    public Flight findByDepartureTown(@PathVariable("departure_town") String departureTown) {
        return service.findByDepartureTown(departureTown);
    }

    @PutMapping("update_flight")
    public Flight updateFlight(@RequestBody Flight flight) {
        return service.updateFlight(flight);
    }

    @PutMapping("update_flight/{departureTown}/passenger/{passengerName}")
    public Flight assignFlightToPassenger(@PathVariable String departureTown,
                                          @PathVariable String passengerName) {
        return service.assignFlightToPassenger(departureTown, passengerName);
    }

    @PutMapping("update_flight/{departureTown}/airline/{airlineName}")
    public Flight assignFlightToAirline(@PathVariable String departureTown,
                                          @PathVariable String airlineName) {
        return service.assignFlightToAirline(departureTown, airlineName);
    }
    @DeleteMapping("delete_flight/{departure_town}")
    public void deleteByDepartureTown(@PathVariable("departure_town") String departureTown) {
        service.deleteByDepartureTown(departureTown);
    }
}
