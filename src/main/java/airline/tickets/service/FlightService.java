package airline.tickets.service;

import airline.tickets.model.Flight;

import java.util.List;

public interface FlightService {
    List<Flight> findAllFlights();
    Flight saveFlight(Flight flight);
    Flight findByDepartureTown(String departureTown);
    Flight updateFlight(Flight flight);
    Flight assignFlightToPassenger(String departureTown, String passengerName);
    Flight assignFlightToAirline(String departureTown, String airlineName);
    void deleteByDepartureTown(String departureTown);
}
