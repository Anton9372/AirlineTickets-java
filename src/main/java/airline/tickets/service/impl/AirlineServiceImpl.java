package airline.tickets.service.impl;

import airline.tickets.dto.AirlineDTO;
import airline.tickets.dto.FlightDTO;
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

    @Override
    public List<AirlineDTO> findAllAirlines() {
        List<Airline> airlineList = airlineRepository.findAll();
        return convertModelToDTO.convertToDTOList(airlineList, convertModelToDTO::airlineConversion);
    }

    @Override
    public Optional<AirlineDTO> findByName(String airlineName) {
        Optional<Airline> optionalAirline = airlineRepository.findByName(airlineName);
        if (optionalAirline.isPresent()) {
            Airline airline = optionalAirline.get();
            return Optional.of(convertModelToDTO.airlineConversion(airline));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public AirlineDTO saveOrUpdateAirline(Airline airline) {
        airlineRepository.save(airline);
        return convertModelToDTO.airlineConversion(airline);
    }

    @Override
    public List<FlightDTO> findAllFlights(String airlineName) {
        Optional<Airline> optionalAirline = airlineRepository.findByName(airlineName);
        if (optionalAirline.isPresent()) {
            List<Flight> flightList = optionalAirline.get().getFlights();
            return convertModelToDTO.convertToDTOList(flightList, convertModelToDTO::flightConversion);
        } else {
            return Collections.emptyList();
        }
    }
}
