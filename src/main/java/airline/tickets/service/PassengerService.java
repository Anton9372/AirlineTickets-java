package airline.tickets.service;

import airline.tickets.dto.FlightDTO;
import airline.tickets.dto.PassengerDTO;
import airline.tickets.dto.ReservationDTO;
import airline.tickets.exception.BadRequestException;
import airline.tickets.exception.ResourceNotFoundException;
import airline.tickets.model.Passenger;

import java.util.List;

public interface PassengerService {

    List<PassengerDTO> findAllPassengers();

    List<PassengerDTO> findByName(String name);

    PassengerDTO saveOrUpdatePassenger(Passenger passenger) throws BadRequestException;

    List<FlightDTO> findAllFlights(Long passengerId) throws ResourceNotFoundException;

    List<ReservationDTO> findAllReservations(Long passengerId) throws ResourceNotFoundException;

    void deletePassenger(Long passengerId) throws ResourceNotFoundException;
}
