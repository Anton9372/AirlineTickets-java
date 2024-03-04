package airline.tickets.service.impl;

import airline.tickets.model.Airline;
import airline.tickets.repository.AirlineRepository;
import airline.tickets.service.AirlineService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class InDBAirlineServiceImpl implements AirlineService {

    private final AirlineRepository repository;

    @Override
    public List<Airline> findAllAirlines() {
        return repository.findAll();
    }

    @Override
    public Airline saveAirline(Airline airline) {
        return repository.save(airline);
    }

    @Override
    public Airline findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public Airline updateAirline(Airline airline) {
        return repository.save(airline);
    }

    @Override
    @Transactional
    public void deleteByName(String name) {
        repository.deleteByName(name);
    }
}
