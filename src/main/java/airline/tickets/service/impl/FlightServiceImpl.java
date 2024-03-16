package airline.tickets.service.impl;

import airline.tickets.dto.FlightDTO;
import airline.tickets.dto.PassengerDTO;
import airline.tickets.dto.TicketDTO;
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
    public FlightDTO saveOrUpdateFlight(Flight flight, String airlineName) {
        Optional<Airline> optionalAirline = airlineRepository.findByName(airlineName);
        if (optionalAirline.isPresent()) {
            Airline airline = optionalAirline.get();
            flight.setAirline(airline);
            flightRepository.save(flight);
            return convertModelToDTO.flightConversion(flight);
        } else {
            return null;
        }
    }

    @Override
    public List<TicketDTO> findAllTickets(Long flightId) {
        Optional<Flight> optionalFlight = flightRepository.findById(flightId);
        if (optionalFlight.isPresent()) {
            List<Ticket> ticketList = optionalFlight.get().getTickets();
            return convertModelToDTO.convertToDTOList(ticketList, convertModelToDTO::ticketConversion);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<PassengerDTO> findAllPassengers(Long flightId) {
        Optional<Flight> optionalFlight = flightRepository.findById(flightId);
        if (optionalFlight.isPresent()) {
            List<Passenger> passengerList = optionalFlight.get().getPassengers();
            return convertModelToDTO.convertToDTOList(passengerList, convertModelToDTO::passengerConversion);
        } else {
            return Collections.emptyList();
        }
    }
}
