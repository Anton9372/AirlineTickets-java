package airline.tickets.controller;

import airline.tickets.aspect.LoggingAnnotation;
import airline.tickets.dto.FlightDTO;
import airline.tickets.dto.PassengerDTO;
import airline.tickets.dto.TicketDTO;
import airline.tickets.model.Flight;
import airline.tickets.service.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "FlightController")
@RestController
@RequestMapping("/api/v1/flights")
@AllArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @Operation(summary = "Просмотр всех рейсов")
    @GetMapping
    public List<FlightDTO> findAllFlights() {
        return flightService.findAllFlights();
    }

    @Operation(summary = "Поиск рейса по Id")
    @GetMapping("/{flight_id}")
    public Optional<FlightDTO> findFlightById(@PathVariable("flight_id") final Long flightId) {
        return flightService.findFlightById(flightId);
    }

    @Operation(summary = "Просмотр всех рейсов по городу отправления")
    @GetMapping("/departure_town/{departure_town}")
    public List<FlightDTO> findFlightByDepartureTown(@PathVariable("departure_town") final String departureTown) {
        return flightService.findFlightByDepartureTown(departureTown);
    }

    @Operation(summary = "Просмотр всех рейсов по городу прибытия")
    @GetMapping("/arrival_town/{arrival_town}")
    public List<FlightDTO> findFlightByArrivalTown(@PathVariable("arrival_town") final String arrivalTown) {
        return flightService.findFlightByArrivalTown(arrivalTown);
    }

    @Operation(summary = "Просмотр всех рейсов по городу отправления и городу прибытия")
    @GetMapping("/departure_town/{departure_town}/arrival_town/{arrival_town}")
    public List<FlightDTO> findByFlightDepartureTownAndArrivalTown(
            @PathVariable("departure_town") final String departureTown,
            @PathVariable("arrival_town") final String arrivalTown) {
        return flightService.findFlightByDepartureTownAndArrivalTown(departureTown, arrivalTown);
    }

    @Operation(summary = "Просмотр всех билетов рейса")
    @GetMapping("/{flight_id}/tickets")
    @LoggingAnnotation
    public List<TicketDTO> findAllFlightTickets(@PathVariable("flight_id") final Long flightId) {
        return flightService.findAllFlightTickets(flightId);
    }

    @Operation(summary = "Просмотр всех пассажиров рейса")
    @GetMapping("/{flight_id}/passengers")
    @LoggingAnnotation
    public List<PassengerDTO> findAllFlightPassengers(@PathVariable("flight_id") final Long flightId) {
        return flightService.findAllFlightPassengers(flightId);
    }

    @Operation(summary = "Добавить рейс")
    @PostMapping("/save_flight/{airline_name}")
    @LoggingAnnotation
    public FlightDTO saveFlight(@Valid @RequestBody final Flight flight,
                                @PathVariable("airline_name") final String airlineName) {
        return flightService.saveOrUpdateFlight(flight, airlineName);
    }

    @Operation(summary = "Изменить рейс")
    @PutMapping("/update_flight/{airline_name}")
    @LoggingAnnotation
    public FlightDTO updateFlight(@Valid @RequestBody final Flight flight,
                                     @PathVariable("airline_name") final String airlineName) {
        return flightService.saveOrUpdateFlight(flight, airlineName);
    }

    @Operation(summary = "Удалить рейс")
    @DeleteMapping("/delete_flight/{flight_id}")
    @LoggingAnnotation
    public void deleteFlight(@PathVariable("flight_id") final Long flightId) {
        flightService.deleteFlight(flightId);
    }
}
