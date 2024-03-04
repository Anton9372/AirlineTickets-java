package airline.tickets.service.impl;

import airline.tickets.model.Flight;
import airline.tickets.service.FlightService;
import airline.tickets.repository.FlightRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class InDBFlightServiceImpl implements FlightService {

    private final FlightRepository repository;

    @Override
    public List<Flight> findAllFlights() {
        return repository.findAll();
    }

    @Override
    public Flight saveFlight(Flight flight) {
        return repository.save(flight);
    }

    @Override
    public Flight findByDepartureTown(String departureTown) {
        return repository.findByDepartureTown(departureTown);
    }

    @Override
    public Flight updateFlight(Flight flight) {
        return repository.save(flight);
    }

    @Override
    @Transactional
    public void deleteByDepartureTown(String departureTown) {
        repository.deleteByDepartureTown(departureTown);
    }
}
