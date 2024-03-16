package airline.tickets.service;

import airline.tickets.dto.FlightDTO;
import airline.tickets.dto.PassengerDTO;
import airline.tickets.dto.ReservationDTO;
import airline.tickets.model.Passenger;

import java.util.List;

public interface PassengerService {

    List<PassengerDTO> findAllPassengers();

    List<PassengerDTO> findByName(String name);

    PassengerDTO saveOrUpdatePassenger(Passenger passenger);

    List<FlightDTO> findAllFlights(Long passengerId);

    List<ReservationDTO> findAllReservations(Long passengerId);

    void deletePassenger(Long passengerId);
}
