package airline.tickets.service;

import airline.tickets.model.Passenger;

import java.util.List;

public interface PassengerService {
    List<Passenger> findAllPassengers();
    Passenger savePassenger(Passenger passenger);
    Passenger findByName(String name);
    Passenger updatePassenger(Passenger passenger);
    void deleteByName(String name);
}
