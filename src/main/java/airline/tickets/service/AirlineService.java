package airline.tickets.service;

import airline.tickets.model.Airline;

import java.util.List;

public interface AirlineService {
    List<Airline> findAllAirlines();
    Airline saveAirline(Airline airline);
    Airline findByName(String name);
    Airline updateAirline(Airline airline);
    void deleteByName(String name);
}
