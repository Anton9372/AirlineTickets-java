package airline.tickets.controller;

import airline.tickets.dto.AirlineDTO;
import airline.tickets.dto.FlightDTO;
import airline.tickets.model.Airline;
import airline.tickets.model.Flight;
import airline.tickets.service.AirlineService;
import airline.tickets.service.FlightService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/airlines")
@AllArgsConstructor
public class AirlineController {

    private final AirlineService airlineService;

    private final FlightService flightService;

    @GetMapping
    public List<AirlineDTO> findAllAirlines() {
        return airlineService.findAllAirlines();
    }

    @GetMapping("/{airline_name}")
    public ResponseEntity<AirlineDTO> findByName(@PathVariable("airline_name") String airlineName) {
        Optional<AirlineDTO> optionalAirlineDTO = airlineService.findByName(airlineName);
        return optionalAirlineDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{airline_name}/flights")
    public List<FlightDTO> findAllFlights(@PathVariable("airline_name") String airlineName) {
        return airlineService.findAllFlights(airlineName);
    }

    @PostMapping("/save_airline")
    public AirlineDTO saveAirline(@RequestBody Airline airline) {
        return airlineService.saveOrUpdateAirline(airline);
    }

    @PostMapping("/{airline_name}/save_flight")
    public FlightDTO saveFlight(@RequestBody Flight flight, @PathVariable("airline_name") String airlineName) {
        return flightService.saveOrUpdateFlight(flight, airlineName);
    }

    @PutMapping("/update_airline")
    public AirlineDTO updateAirline(@RequestBody Airline airline) {
        return airlineService.saveOrUpdateAirline(airline);
    }

    @PutMapping("/{airline_name}/update_airline")
    public FlightDTO updateFlight(@RequestBody Flight flight, @PathVariable("airline_name") String airlineName) {
        return flightService.saveOrUpdateFlight(flight, airlineName);
    }
}
