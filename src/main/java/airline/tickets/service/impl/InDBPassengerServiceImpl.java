package airline.tickets.service.impl;

import airline.tickets.model.Flight;
import airline.tickets.model.Passenger;
import airline.tickets.repository.PassengerRepository;
import airline.tickets.service.PassengerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InDBPassengerServiceImpl implements PassengerService {

    private final PassengerRepository repository;

    @Override
    public List<Passenger> findAllPassengers() {
        return repository.findAll();
    }

    @Override
    public Passenger savePassenger(Passenger passenger) {
        return repository.save(passenger);
    }

    @Override
    public Passenger findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public Passenger updatePassenger(Passenger passenger) {
        return repository.save(passenger);
    }

    @Override
    @Transactional
    public void deleteByName(String name) {
        repository.deleteByName(name);
    }
}
