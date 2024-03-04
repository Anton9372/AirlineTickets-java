package airline.tickets.controller;

import airline.tickets.model.Passenger;
import airline.tickets.service.PassengerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/passengers")
@AllArgsConstructor
public class PassengerController {

    private final PassengerService service;

    @GetMapping
    public List<Passenger> findAllPassengers() {
        return service.findAllPassengers();
    }

    @PostMapping("save_passenger")
    public Passenger savePassenger(@RequestBody Passenger passenger) {
        return service.savePassenger(passenger);
    }

    @GetMapping("/{name}")
    public Passenger findByName(@PathVariable("name") String name) {
        return service.findByName(name);
    }

    @PutMapping("update_passenger")
    public Passenger updatePassenger(@RequestBody Passenger passenger) {
        return service.updatePassenger(passenger);
    }

    @DeleteMapping("delete_passenger/{name}")
    public void deleteByName(@PathVariable("name") String name) {
        service.deleteByName(name);
    }
}
