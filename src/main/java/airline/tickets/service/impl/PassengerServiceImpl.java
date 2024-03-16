package airline.tickets.service.impl;

import airline.tickets.dto.FlightDTO;
import airline.tickets.dto.PassengerDTO;
import airline.tickets.dto.ReservationDTO;
import airline.tickets.model.Flight;
import airline.tickets.model.Passenger;
import airline.tickets.model.Reservation;
import airline.tickets.repository.PassengerRepository;
import airline.tickets.service.PassengerService;
import airline.tickets.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;
    private final ReservationService reservationService;
    private final ConvertModelToDTOImpl convertModelToDTO;

    @Override
    public List<PassengerDTO> findAllPassengers() {
        List<Passenger> passengerList = passengerRepository.findAll();
        return convertModelToDTO.convertToDTOList(passengerList, convertModelToDTO::passengerConversion);
    }

    @Override
    public List<PassengerDTO> findByName(String name) {
        List<Passenger> passengerList = passengerRepository.findByName(name);
        return convertModelToDTO.convertToDTOList(passengerList, convertModelToDTO::passengerConversion);
    }

    @Override
    public PassengerDTO saveOrUpdatePassenger(Passenger passenger) {
        passengerRepository.save(passenger);
        return convertModelToDTO.passengerConversion(passenger);
    }

    @Override
    public List<FlightDTO> findAllFlights(Long passengerId) {
        Optional<Passenger> optionalPassenger = passengerRepository.findById(passengerId);
        if (optionalPassenger.isPresent()) {
            List<Flight> flightList = optionalPassenger.get().getFlights();
            return convertModelToDTO.convertToDTOList(flightList, convertModelToDTO::flightConversion);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<ReservationDTO> findAllReservations(Long passengerId) {
        Optional<Passenger> optionalPassenger = passengerRepository.findById(passengerId);
        if (optionalPassenger.isPresent()) {
            List<Reservation> reservationList = optionalPassenger.get().getReservations();
            return convertModelToDTO.convertToDTOList(reservationList, convertModelToDTO::reservationConversion);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void deletePassenger(Long passengerId) {
        Optional<Passenger> optionalPassenger = passengerRepository.findById(passengerId);
        if (optionalPassenger.isPresent()) {
            Passenger passenger = optionalPassenger.get();
            List<Reservation> reservationList = passenger.getReservations();
            Iterator<Reservation> iterator = reservationList.iterator();
            while (iterator.hasNext()) {
                Reservation reservation = iterator.next();
                iterator.remove();
                reservationService.deleteReservation(reservation.getId());
            }
            passengerRepository.delete(passenger);
        }
    }
}
