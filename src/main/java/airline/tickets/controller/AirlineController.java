package airline.tickets.controller;

import airline.tickets.aspect.LoggingAnnotation;
import airline.tickets.dto.AirlineDTO;
import airline.tickets.dto.FlightDTO;
import airline.tickets.exception.BadRequestException;
import airline.tickets.model.Airline;
import airline.tickets.service.AirlineService;
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

    @Operation(summary = "Просмотр всех авиакомпаний")
    @GetMapping
    public List<AirlineDTO> findAllAirlines() {
        return airlineService.findAllAirlines();
    }

    @Operation(summary = "Найти авиакомпанию по имени")
    @GetMapping("/{airline_name}")
    @LoggingAnnotation
    public Optional<AirlineDTO> findAirlineByName(@PathVariable("airline_name") final String airlineName) {
        return airlineService.findAirlineByName(airlineName);
    }

    @Operation(summary = "Просмотр всех рейсов авиакомпании")
    @GetMapping("/{airline_name}/flights")
    @LoggingAnnotation
    public List<FlightDTO> findAllAirlineFlights(@PathVariable("airline_name") final String airlineName) {
        return airlineService.findAllAirlineFlights(airlineName);
    }

    @Operation(summary = "Добавить авиакомпанию")
    @PostMapping("/save_airline")
    @LoggingAnnotation
    public AirlineDTO saveAirline(@Valid @RequestBody final Airline airline) throws BadRequestException {
        return airlineService.saveOrUpdateAirline(airline);
    }

    @Operation(summary = "Изменить авиакомпанию")
    @PutMapping("/update_airline")
    @LoggingAnnotation
    public AirlineDTO updateAirline(@Valid @RequestBody final Airline airline) {
        return airlineService.saveOrUpdateAirline(airline);
    }

    @Operation(summary = "Удалить авиакомпанию")
    @DeleteMapping("/delete_airline/{airline_name}")
    @LoggingAnnotation
    public void deleteAirline(@PathVariable("airline_name") final String airlineName) {
        airlineService.deleteAirline(airlineName);
    }
}
