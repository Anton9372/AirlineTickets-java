package airline.tickets.service;

import airline.tickets.dto.AirlineDTO;
import airline.tickets.dto.FlightDTO;
import airline.tickets.exception.BadRequestException;
import airline.tickets.exception.ResourceNotFoundException;
import airline.tickets.model.Airline;

import java.util.List;
import java.util.Optional;

public interface AirlineService {
    List<AirlineDTO> findAllAirlines();

    Optional<AirlineDTO> findByName(String airlineName) throws ResourceNotFoundException;

    AirlineDTO saveOrUpdateAirline(Airline airline) throws BadRequestException;

    List<FlightDTO> findAllFlights(String airlineName) throws ResourceNotFoundException;
}
