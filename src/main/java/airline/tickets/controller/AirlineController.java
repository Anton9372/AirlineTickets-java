package airline.tickets.controller;

import airline.tickets.aspect.AspectAnnotation;
import airline.tickets.dto.AirlineDTO;
import airline.tickets.dto.FlightDTO;
import airline.tickets.exception.BadRequestException;
import airline.tickets.model.Airline;
import airline.tickets.model.Flight;
import airline.tickets.service.AirlineService;
import airline.tickets.service.FlightService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "AirlineController")
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
    @AspectAnnotation
    public Optional<AirlineDTO> findByName(@PathVariable("airline_name") final String airlineName) {
        return airlineService.findByName(airlineName);
    }

    @GetMapping("/{airline_name}/flights")
    @AspectAnnotation
    public List<FlightDTO> findAllFlights(@PathVariable("airline_name") final String airlineName) {
        return airlineService.findAllFlights(airlineName);
    }

    @PostMapping("/save_airline")
    @AspectAnnotation
    public AirlineDTO saveAirline(@Valid @RequestBody final Airline airline) throws BadRequestException {
        return airlineService.saveOrUpdateAirline(airline);
    }

    @PostMapping("/{airline_name}/save_flight")
    @AspectAnnotation
    public FlightDTO saveFlight(@Valid @RequestBody final Flight flight,
                                @PathVariable("airline_name") final String airlineName) {
        return flightService.saveOrUpdateFlight(flight, airlineName);
    }

    @PutMapping("/update_airline")
    @AspectAnnotation
    public AirlineDTO updateAirline(@Valid @RequestBody final Airline airline) {
        return airlineService.saveOrUpdateAirline(airline);
    }

    @PutMapping("/{airline_name}/update_airline")
    @AspectAnnotation
    public FlightDTO updateFlight(@Valid @RequestBody final Flight flight,
                                  @PathVariable("airline_name") final String airlineName) {
        return flightService.saveOrUpdateFlight(flight, airlineName);
    }
}
