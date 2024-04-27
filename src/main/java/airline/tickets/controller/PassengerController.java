package airline.tickets.controller;

import airline.tickets.aspect.LoggingAnnotation;
import airline.tickets.dto.PassengerDTO;
import airline.tickets.dto.ReservationDTO;
import airline.tickets.model.Passenger;
import airline.tickets.service.PassengerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "PassengerController")
@RestController
@RequestMapping("/api/v1/passengers")
@AllArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    @Operation(summary = "Просмотр всех пассажиров")
    @GetMapping
    public List<PassengerDTO> findAllPassengers() {
        return passengerService.findAllPassengers();
    }

    @Operation(summary = "Найти пассажира по id")
    @GetMapping("/{passenger_id}")
    public Optional<PassengerDTO> findPassengerById(@PathVariable("passenger_id") final Long passengerId) {
        return passengerService.findPassengerById(passengerId);
    }

    @Operation(summary = "Просмотреть все бронирования пассажира")
    @GetMapping("/{passenger_id}/reservations")
    @LoggingAnnotation
    public List<ReservationDTO> findAllPassengerReservations(@PathVariable("passenger_id") final Long passengerId) {
        return passengerService.findAllPassengerReservations(passengerId);
    }

    @Operation(summary = "Добавить пассажира")
    @PostMapping("/save_passenger")
    @LoggingAnnotation
    public PassengerDTO savePassenger(@Valid @RequestBody final Passenger passenger) {
        return passengerService.saveOrUpdatePassenger(passenger);
    }

    @Operation(summary = "Изменить пассажира")
    @PutMapping("/update_passenger")
    @LoggingAnnotation
    public PassengerDTO updatePassenger(@Valid @RequestBody final Passenger passenger) {
        return passengerService.saveOrUpdatePassenger(passenger);
    }

    @Operation(summary = "Удалить пассажира")
    @DeleteMapping("/delete_passenger/{passenger_id}")
    @LoggingAnnotation
    public void deletePassenger(@PathVariable("passenger_id") final Long passengerId) {
        passengerService.deletePassenger(passengerId);
    }
}
