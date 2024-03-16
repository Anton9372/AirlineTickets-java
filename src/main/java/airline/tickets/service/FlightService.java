package airline.tickets.service;

import airline.tickets.dto.FlightDTO;
import airline.tickets.dto.PassengerDTO;
import airline.tickets.dto.TicketDTO;
import airline.tickets.model.Flight;

import java.util.List;

public interface FlightService {

    List<FlightDTO> findAllFlights();

    List<FlightDTO> findByDepartureTown(String departureTown);

    List<FlightDTO> findByArrivalTown(String arrivalTown);

    List<FlightDTO> findByDepartureTownAndArrivalTown(String departureTown, String arrivalTown);

    FlightDTO saveOrUpdateFlight(Flight flight, String airlineName);

    List<TicketDTO> findAllTickets(Long flightId);

    List<PassengerDTO> findAllPassengers(Long flightId);
}
