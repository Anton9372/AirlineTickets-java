package airline.tickets.service;

import airline.tickets.aspect.LoggingAnnotation;
import airline.tickets.aspect.RequestCounterAnnotation;
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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;
    private final AirlineRepository airlineRepository;

    private final TicketService ticketService;

    private final ConvertModelToDTO convertModelToDTO;

    private static final String NO_AIRLINE_EXIST = "No Airline found with name: ";
    private static final String NO_FLIGHT_EXIST = "No Flight found with id: ";

    @RequestCounterAnnotation
    public List<FlightDTO> findAllFlights() {
        List<Flight> flightList = flightRepository.findAll();
        return convertModelToDTO.convertToDTOList(flightList, convertModelToDTO::flightConversion);
    }

    public Optional<FlightDTO> findFlightById(final Long flightId) throws ResourceNotFoundException {
        Flight flight = flightRepository.findById(flightId).
                orElseThrow(() -> new ResourceNotFoundException(NO_FLIGHT_EXIST + flightId));
        return Optional.of(convertModelToDTO.flightConversion(flight));
    }

    public List<FlightDTO> findFlightByDepartureTown(final String departureTown) {
        List<Flight> flightList = flightRepository.findByDepartureTown(departureTown);
        return convertModelToDTO.convertToDTOList(flightList, convertModelToDTO::flightConversion);
    }

    public List<FlightDTO> findFlightByArrivalTown(final String arrivalTown) {
        List<Flight> flightList = flightRepository.findByArrivalTown(arrivalTown);
        return convertModelToDTO.convertToDTOList(flightList, convertModelToDTO::flightConversion);
    }

    public List<FlightDTO> findFlightByDepartureTownAndArrivalTown(final String departureTown,
                                                                   final String arrivalTown) {
        List<Flight> flightList = flightRepository.findByDepartureTownAndArrivalTown(departureTown, arrivalTown);
        return convertModelToDTO.convertToDTOList(flightList, convertModelToDTO::flightConversion);
    }

    @LoggingAnnotation
    public List<TicketDTO> findAllFlightTickets(final Long flightId) throws ResourceNotFoundException {
        Flight flight = flightRepository.findById(flightId).
                orElseThrow(() -> new ResourceNotFoundException(NO_FLIGHT_EXIST + flightId));
        List<Ticket> ticketList = flight.getTickets();
        return convertModelToDTO.convertToDTOList(ticketList, convertModelToDTO::ticketConversion);
    }

    @LoggingAnnotation
    public List<PassengerDTO> findAllFlightPassengers(final Long flightId) throws ResourceNotFoundException {
        Flight flight = flightRepository.findById(flightId).
                orElseThrow(() -> new ResourceNotFoundException(NO_FLIGHT_EXIST + flightId));
        List<Passenger> passengerList = flight.getPassengers();
        return convertModelToDTO.convertToDTOList(passengerList, convertModelToDTO::passengerConversion);
    }

    @LoggingAnnotation
    public FlightDTO saveOrUpdateFlight(final Flight flight, final String airlineName) throws ResourceNotFoundException,
            BadRequestException {
        Airline airline = airlineRepository.findByName(airlineName).
                orElseThrow(() -> new ResourceNotFoundException(NO_AIRLINE_EXIST + airlineName));
        if (flight.getDepartureTown() == null || flight.getArrivalTown() == null
                || flight.getDepartureDateTime() == null) {
            throw new BadRequestException("departureTown, arrivalTown and departureDateTime must be provided");
        }
        flight.setAirline(airline);
        flightRepository.save(flight);
        return convertModelToDTO.flightConversion(flight);
    }

    @LoggingAnnotation
    public void deleteFlight(final Long flightId) throws ResourceNotFoundException {
        Flight flight = flightRepository.findById(flightId).
                orElseThrow(() -> new ResourceNotFoundException(NO_FLIGHT_EXIST + flightId));

        List<Ticket> ticketList = flight.getTickets();
        Iterator<Ticket> iterator = ticketList.iterator();
        while (iterator.hasNext()) {
            Ticket ticket = iterator.next();
            iterator.remove();
            ticketService.deleteTicket(ticket.getId());
        }

        Airline airline = flight.getAirline();
        List<Flight> flightList = airline.getFlights();
        flightList.remove(flight);
        airline.setFlights(flightList);
        airlineRepository.save(airline);

        flightRepository.delete(flight);
    }
}
