package airline.tickets.service;

import airline.tickets.dto.AirlineDTO;
import airline.tickets.dto.FlightDTO;
import airline.tickets.model.Airline;

import java.util.List;
import java.util.Optional;

public interface AirlineService {
    List<AirlineDTO> findAllAirlines();

    Optional<AirlineDTO> findByName(String airlineName);

    AirlineDTO saveOrUpdateAirline(Airline airline);

    List<FlightDTO> findAllFlights(String airlineName);
}
