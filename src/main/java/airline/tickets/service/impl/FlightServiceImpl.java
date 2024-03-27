package airline.tickets.service.impl;

import airline.tickets.aspect.AspectAnnotation;
import airline.tickets.dto.FlightDTO;
import airline.tickets.dto.PassengerDTO;
import airline.tickets.dto.TicketDTO;
import airline.tickets.exception.BadRequestException;
import airline.tickets.exception.ResourceNotFoundException;
import airline.tickets.model.Airline;
import airline.tickets.model.Flight;
import airline.tickets.model.Passenger;
import airline.tickets.model.Ticket;
import airline.tickets.repository.AirlineRepository;
import airline.tickets.repository.FlightRepository;
import airline.tickets.service.FlightService;
import airline.tickets.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final AirlineRepository airlineRepository;

    private final ConvertModelToDTOImpl convertModelToDTO;

    private static final String NO_AIRLINE_EXIST = "No Airline found with name: ";
    private static final String NO_FLIGHT_EXIST = "No Flight found with id: ";

    @Override
    public List<FlightDTO> findAllFlights() {
        List<Flight> flightList = flightRepository.findAll();
        return convertModelToDTO.convertToDTOList(flightList, convertModelToDTO::flightConversion);
    }

    @Override
    public List<FlightDTO> findByDepartureTown(String departureTown) {
        List<Flight> flightList = flightRepository.findByDepartureTown(departureTown);
        return convertModelToDTO.convertToDTOList(flightList, convertModelToDTO::flightConversion);
    }

    @Override
    public List<FlightDTO> findByArrivalTown(String arrivalTown) {
        List<Flight> flightList = flightRepository.findByArrivalTown(arrivalTown);
        return convertModelToDTO.convertToDTOList(flightList, convertModelToDTO::flightConversion);
    }

    @Override
    public List<FlightDTO> findByDepartureTownAndArrivalTown(String departureTown, String arrivalTown) {
        List<Flight> flightList = flightRepository.findByDepartureTownAndArrivalTown(departureTown, arrivalTown);
        return convertModelToDTO.convertToDTOList(flightList, convertModelToDTO::flightConversion);
    }

    @Override
    @AspectAnnotation
    public FlightDTO saveOrUpdateFlight(Flight flight, String airlineName) throws ResourceNotFoundException,
            BadRequestException {
        Airline airline = airlineRepository.findByName(airlineName).
                orElseThrow(() -> new ResourceNotFoundException(NO_AIRLINE_EXIST + airlineName));
        if(flight.getDepartureTown() == null || flight.getArrivalTown() == null ||
                flight.getDepartureDateTime() == null) {
            throw new BadRequestException("departureTown, arrivalTown and departureDateTime must be provided");
        }
        flight.setAirline(airline);
        flightRepository.save(flight);
        return convertModelToDTO.flightConversion(flight);
    }

    @Override
    @AspectAnnotation
    public List<TicketDTO> findAllTickets(Long flightId) throws ResourceNotFoundException {
        Flight flight = flightRepository.findById(flightId).
                orElseThrow(() -> new ResourceNotFoundException(NO_FLIGHT_EXIST + flightId));
        List<Ticket> ticketList = flight.getTickets();
        return convertModelToDTO.convertToDTOList(ticketList, convertModelToDTO::ticketConversion);
    }

    @Override
    @AspectAnnotation
    public List<PassengerDTO> findAllPassengers(Long flightId) throws ResourceNotFoundException {
        Flight flight = flightRepository.findById(flightId).
                orElseThrow(() -> new ResourceNotFoundException(NO_FLIGHT_EXIST + flightId));
        List<Passenger> passengerList = flight.getPassengers();
        return convertModelToDTO.convertToDTOList(passengerList, convertModelToDTO::passengerConversion);
    }
}
