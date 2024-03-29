package airline.tickets.service.impl;

import airline.tickets.aspect.AspectAnnotation;
import airline.tickets.dto.FlightDTO;
import airline.tickets.dto.PassengerDTO;
import airline.tickets.dto.ReservationDTO;
import airline.tickets.exception.BadRequestException;
import airline.tickets.exception.ResourceNotFoundException;
import airline.tickets.model.Flight;
import airline.tickets.model.Passenger;
import airline.tickets.model.Reservation;
import airline.tickets.repository.PassengerRepository;
import airline.tickets.service.PassengerService;
import airline.tickets.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
@AllArgsConstructor
public class PassengerServiceImpl implements PassengerService {

    private final PassengerRepository passengerRepository;
    private final ReservationService reservationService;

    private final ConvertModelToDTOImpl convertModelToDTO;

    private static final String NO_PASSENGER_EXIST = "No Passenger found with id: ";

    @Override
    public List<PassengerDTO> findAllPassengers() {
        List<Passenger> passengerList = passengerRepository.findAll();
        return convertModelToDTO.convertToDTOList(passengerList, convertModelToDTO::passengerConversion);
    }

    @Override
    public List<PassengerDTO> findByName(final String name) {
        List<Passenger> passengerList = passengerRepository.findByName(name);
        return convertModelToDTO.convertToDTOList(passengerList, convertModelToDTO::passengerConversion);
    }

    @Override
    @AspectAnnotation
    public PassengerDTO saveOrUpdatePassenger(final Passenger passenger) throws BadRequestException {
        if (passenger.getName() == null || passenger.getPassportNumber() == null) {
            throw new BadRequestException("name and passportNumber must be provided");
        }
        passengerRepository.save(passenger);
        return convertModelToDTO.passengerConversion(passenger);
    }

    @Override
    @AspectAnnotation
    public List<FlightDTO> findAllFlights(final Long passengerId) throws ResourceNotFoundException {
        Passenger passenger = passengerRepository.findById(passengerId).
                orElseThrow(() -> new ResourceNotFoundException(NO_PASSENGER_EXIST + passengerId));
        List<Flight> flightList = passenger.getFlights();
        return convertModelToDTO.convertToDTOList(flightList, convertModelToDTO::flightConversion);
    }

    @Override
    @AspectAnnotation
    public List<ReservationDTO> findAllReservations(final Long passengerId) throws ResourceNotFoundException {
        Passenger passenger = passengerRepository.findById(passengerId).
                orElseThrow(() -> new ResourceNotFoundException(NO_PASSENGER_EXIST + passengerId));
        List<Reservation> reservationList = passenger.getReservations();
        return convertModelToDTO.convertToDTOList(reservationList, convertModelToDTO::reservationConversion);
    }

    @Override
    @AspectAnnotation
    public void deletePassenger(final Long passengerId) throws ResourceNotFoundException {
        Passenger passenger = passengerRepository.findById(passengerId).
                orElseThrow(() -> new ResourceNotFoundException(NO_PASSENGER_EXIST + passengerId));
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
