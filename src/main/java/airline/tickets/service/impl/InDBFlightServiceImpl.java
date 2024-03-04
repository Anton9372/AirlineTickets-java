package airline.tickets.service.impl;

import airline.tickets.model.Airline;
import airline.tickets.model.Flight;
import airline.tickets.model.Passenger;
import airline.tickets.repository.AirlineRepository;
import airline.tickets.repository.PassengerRepository;
import airline.tickets.service.FlightService;
import airline.tickets.repository.FlightRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class InDBFlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;

    private final PassengerRepository passengerRepository;

    private final AirlineRepository airlineRepository;

    @Override
    public List<Flight> findAllFlights() {
        return flightRepository.findAll();
    }

    @Override
    public Flight saveFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    @Override
    public Flight findByDepartureTown(String departureTown) {
        return flightRepository.findByDepartureTown(departureTown);
    }

    @Override
    public Flight updateFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    @Override
    public Flight assignFlightToPassenger(String departureTown, String passengerName) {
        Flight flight = flightRepository.findByDepartureTown(departureTown);
        Passenger passenger = passengerRepository.findByName(passengerName);
        List<Passenger> passengerList = flight.getPassengers();
        passengerList.add(passenger);
        flight.setPassengers(passengerList);
        return flightRepository.save(flight);
    }

    @Override
    public Flight assignFlightToAirline(String departureTown, String airlineName) {
        Flight flight = flightRepository.findByDepartureTown(departureTown);
        Airline airline = airlineRepository.findByName(airlineName);
        flight.setAirline(airline);
        return flightRepository.save(flight);
    }

    @Override
    @Transactional
    public void deleteByDepartureTown(String departureTown) {
        flightRepository.deleteByDepartureTown(departureTown);
    }
}
