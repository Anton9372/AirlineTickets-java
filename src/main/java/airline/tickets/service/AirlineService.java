package airline.tickets.service;

import airline.tickets.aspect.AspectAnnotation;
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

    private static final String NO_AIRLINE_EXIST = "No Airline found with name: ";

    public List<AirlineDTO> findAllAirlines() {
        List<Airline> airlineList = airlineRepository.findAll();
        return convertModelToDTO.convertToDTOList(airlineList, convertModelToDTO::airlineConversion);
    }

    public Optional<AirlineDTO> findAirlineByName(final String airlineName) throws ResourceNotFoundException {
        Airline airline = airlineRepository.findByName(airlineName).
                orElseThrow(() -> new ResourceNotFoundException(NO_AIRLINE_EXIST + airlineName));
        return Optional.of(convertModelToDTO.airlineConversion(airline));
    }

    @AspectAnnotation
    public List<FlightDTO> findAllAirlineFlights(final String airlineName) throws ResourceNotFoundException {
        Airline airline = airlineRepository.findByName(airlineName).
                orElseThrow(() -> new ResourceNotFoundException(NO_AIRLINE_EXIST + airlineName));
        List<Flight> flightList = airline.getFlights();
        return convertModelToDTO.convertToDTOList(flightList, convertModelToDTO::flightConversion);
    }

    @AspectAnnotation
    public AirlineDTO saveOrUpdateAirline(final Airline airline) throws BadRequestException {
        if (airline.getName() == null) {
            throw new BadRequestException("No name provided");
        }
        airlineRepository.save(airline);
        return convertModelToDTO.airlineConversion(airline);
    }

    @AspectAnnotation
    public void deleteAirline(final String airlineName) throws ResourceNotFoundException {
        Airline airline = airlineRepository.findByName(airlineName).
                orElseThrow(() -> new ResourceNotFoundException(NO_AIRLINE_EXIST + airlineName));

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
