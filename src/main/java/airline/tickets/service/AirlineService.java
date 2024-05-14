package airline.tickets.service;

import airline.tickets.aspect.LoggingAnnotation;
import airline.tickets.aspect.RequestCounterAnnotation;
import airline.tickets.dto.AirlineDTO;
import airline.tickets.dto.FlightDTO;
import airline.tickets.exception.BadRequestException;
import airline.tickets.exception.ResourceNotFoundException;
import airline.tickets.model.Airline;
import airline.tickets.model.Flight;
import airline.tickets.repository.AirlineRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AirlineService {

    private final AirlineRepository airlineRepository;

    private final FlightService flightService;

    private final ConvertModelToDTO convertModelToDTO;
    private static final String NO_AIRLINE_EXIST = "No Airline found with id: ";

    @RequestCounterAnnotation
    public List<AirlineDTO> findAllAirlines() {
        List<Airline> airlineList = airlineRepository.findAll();
        return convertModelToDTO.convertToDTOList(airlineList, convertModelToDTO::airlineConversion);
    }

    public Optional<AirlineDTO> findAirlineById(final Long airlineId) throws ResourceNotFoundException {
        Airline airline = airlineRepository.findById(airlineId).
                orElseThrow(() -> new ResourceNotFoundException(NO_AIRLINE_EXIST + airlineId));
        return Optional.of(convertModelToDTO.airlineConversion(airline));
    }

    public List<AirlineDTO> findAirlinesByName(final String airlineName) {
        List<Airline> airlineList = airlineRepository.findByName(airlineName);
        return convertModelToDTO.convertToDTOList(airlineList, convertModelToDTO::airlineConversion);
    }

    public List<FlightDTO> findAllAirlineFlights(final Long airlineId) throws ResourceNotFoundException {
        Airline airline = airlineRepository.findById(airlineId).
                orElseThrow(() -> new ResourceNotFoundException(NO_AIRLINE_EXIST + airlineId));
        List<Flight> flightList = airline.getFlights();
        return convertModelToDTO.convertToDTOList(flightList, convertModelToDTO::flightConversion);
    }

    @LoggingAnnotation
    public AirlineDTO saveOrUpdateAirline(final Airline airline) throws BadRequestException {
        if (airline.getName() == null) {
            throw new BadRequestException("No airlineName provided");
        }
        airlineRepository.save(airline);
        return convertModelToDTO.airlineConversion(airline);
    }

    @LoggingAnnotation
    public void deleteAirline(final Long airlineId) throws ResourceNotFoundException {
        Airline airline = airlineRepository.findById(airlineId).
                orElseThrow(() -> new ResourceNotFoundException(NO_AIRLINE_EXIST + airlineId));

        List<Flight> flightList = airline.getFlights();
        Iterator<Flight> iterator = flightList.iterator();
        while (iterator.hasNext()) {
            Flight flight = iterator.next();
            iterator.remove();
            flightService.deleteFlight(flight.getId());
        }

        airlineRepository.delete(airline);
    }
}
