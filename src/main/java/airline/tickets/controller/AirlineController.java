package airline.tickets.controller;

import airline.tickets.model.Airline;
import airline.tickets.service.AirlineService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/airlines")
@AllArgsConstructor
public class AirlineController {

    private final AirlineService service;

    @GetMapping
    public List<Airline> findAllAirlines() {
        return service.findAllAirlines();
    }

    @PostMapping("save_airline")
    public Airline saveAirline(@RequestBody Airline airline) {
        return service.saveAirline(airline);
    }

    @GetMapping("/{name}")
    public Airline findByName(@PathVariable("name") String name) {
        return service.findByName(name);
    }

    @PutMapping("update_airline")
    public Airline updateAirline(@RequestBody Airline airline) {
        return service.updateAirline(airline);
    }

    @DeleteMapping("delete_airline/{name}")
    public void deleteByName(@PathVariable("name") String name) {
        service.deleteByName(name);
    }
}
