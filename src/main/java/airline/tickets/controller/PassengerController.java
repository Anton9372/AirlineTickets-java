package airline.tickets.controller;

import airline.tickets.dto.FlightDTO;
import airline.tickets.dto.PassengerDTO;
import airline.tickets.dto.ReservationDTO;
import airline.tickets.model.Passenger;
import airline.tickets.service.PassengerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/passengers")
@AllArgsConstructor
public class PassengerController {

    private final PassengerService passengerService;

    @GetMapping
    public List<PassengerDTO> findAllPassengers() {
        return passengerService.findAllPassengers();
    }

    @GetMapping("/{passenger_name}")
    public List<PassengerDTO> findByName(@PathVariable("passenger_name") String passengerName) {
        return passengerService.findByName(passengerName);
    }

    @GetMapping("/{passenger_id}/flights")
    public List<FlightDTO> findAllFlights(@PathVariable("passenger_id") Long passengerId) {
        return passengerService.findAllFlights(passengerId);
    }

    @GetMapping("/{passenger_id}/reservations")
    public List<ReservationDTO> findAllReservations(@PathVariable("passenger_id") Long passengerId) {
        return passengerService.findAllReservations(passengerId);
    }

    @PostMapping("/save_passenger")
    public PassengerDTO savePassenger(@RequestBody Passenger passenger) {
        return passengerService.saveOrUpdatePassenger(passenger);
    }

    @PutMapping("/update_passenger")
    public PassengerDTO updatePassenger(@RequestBody Passenger passenger) {
        return passengerService.saveOrUpdatePassenger(passenger);
    }

    @DeleteMapping("/delete_passenger/{passenger_id}")
    public void deletePassenger(@PathVariable("passenger_id") Long passengerId) {
        passengerService.deletePassenger(passengerId);
    }
}