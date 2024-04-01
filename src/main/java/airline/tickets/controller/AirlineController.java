package airline.tickets.controller;

import airline.tickets.aspect.AspectAnnotation;
import airline.tickets.dto.AirlineDTO;
import airline.tickets.dto.FlightDTO;
import airline.tickets.exception.BadRequestException;
import airline.tickets.model.Airline;
import airline.tickets.model.Flight;
import airline.tickets.service.AirlineService;
import airline.tickets.service.FlightService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Просмотр всех авиакомпаний")
    @GetMapping
    public List<AirlineDTO> findAllAirlines() {
        return airlineService.findAllAirlines();
    }

    @Operation(summary = "Найти авиакомпанию по имени")
    @GetMapping("/{airline_name}")
    @AspectAnnotation
    public Optional<AirlineDTO> findAirlineByName(@PathVariable("airline_name") final String airlineName) {
        return airlineService.findAirlineByName(airlineName);
    }

    @Operation(summary = "Просмотр всех рейсов авиакомпании")
    @GetMapping("/{airline_name}/flights")
    @AspectAnnotation
    public List<FlightDTO> findAllAirlineFlights(@PathVariable("airline_name") final String airlineName) {
        return airlineService.findAllAirlineFlights(airlineName);
    }

    @Operation(summary = "Добавить авиакомпанию")
    @PostMapping("/save_airline")
    @AspectAnnotation
    public AirlineDTO saveAirline(@Valid @RequestBody final Airline airline) throws BadRequestException {
        return airlineService.saveOrUpdateAirline(airline);
    }

    @Operation(summary = "Добавить рейс для авиакомпании")
    @PostMapping("/{airline_name}/save_flight")
    @AspectAnnotation
    public FlightDTO saveAirlineFlight(@Valid @RequestBody final Flight flight,
                                @PathVariable("airline_name") final String airlineName) {
        return flightService.saveOrUpdateFlight(flight, airlineName);
    }

    @Operation(summary = "Изменить авиакомпанию")
    @PutMapping("/update_airline")
    @AspectAnnotation
    public AirlineDTO updateAirline(@Valid @RequestBody final Airline airline) {
        return airlineService.saveOrUpdateAirline(airline);
    }

    @Operation(summary = "Обновить рейс авиакомпании")
    @PutMapping("/{airline_name}/update_airline")
    @AspectAnnotation
    public FlightDTO updateAirlineFlight(@Valid @RequestBody final Flight flight,
                                  @PathVariable("airline_name") final String airlineName) {
        return flightService.saveOrUpdateFlight(flight, airlineName);
    }

    @Operation(summary = "Удалить авиакомпанию")
    @DeleteMapping("/delete_airline/{airline_name}")
    @AspectAnnotation
    public void deleteAirline(@PathVariable("airline_name") final String airlineName) {
        airlineService.deleteAirline(airlineName);
    }
}
