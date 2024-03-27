package airline.tickets.service.impl;

import airline.tickets.aspect.AspectAnnotation;
import airline.tickets.dto.AirlineDTO;
import airline.tickets.dto.FlightDTO;
import airline.tickets.exception.BadRequestException;
import airline.tickets.exception.ResourceNotFoundException;
import airline.tickets.model.Airline;
import airline.tickets.model.Flight;
import airline.tickets.repository.AirlineRepository;
import airline.tickets.service.AirlineService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AirlineServiceImpl implements AirlineService {

    private final AirlineRepository airlineRepository;

    private final ConvertModelToDTOImpl convertModelToDTO;

    private static final String NO_AIRLINE_EXIST = "No Airline found with name: ";

    @Override
    public List<AirlineDTO> findAllAirlines() {
        List<Airline> airlineList = airlineRepository.findAll();
        return convertModelToDTO.convertToDTOList(airlineList, convertModelToDTO::airlineConversion);
    }

    @Override
    @AspectAnnotation
    public Optional<AirlineDTO> findByName(String airlineName) throws ResourceNotFoundException {
        Airline airline = airlineRepository.findByName(airlineName).
                orElseThrow(() -> new ResourceNotFoundException(NO_AIRLINE_EXIST + airlineName));
        return Optional.of(convertModelToDTO.airlineConversion(airline));
    }

    @Override
    @AspectAnnotation
    public AirlineDTO saveOrUpdateAirline(Airline airline) throws BadRequestException {
        if(airline.getName() == null) {
            throw new BadRequestException("No name provided");
        }
        airlineRepository.save(airline);
        return convertModelToDTO.airlineConversion(airline);
    }

    @Override
    @AspectAnnotation
    public List<FlightDTO> findAllFlights(String airlineName) throws ResourceNotFoundException {
        Airline airline = airlineRepository.findByName(airlineName).
                orElseThrow(() -> new ResourceNotFoundException(NO_AIRLINE_EXIST + airlineName));
        List<Flight> flightList = airline.getFlights();
        return convertModelToDTO.convertToDTOList(flightList, convertModelToDTO::flightConversion);
    }
}
