package airline.tickets.service;

import airline.tickets.aspect.LoggingAnnotation;
import airline.tickets.aspect.RequestCounterAnnotation;
import airline.tickets.dto.FlightDTO;
import airline.tickets.dto.PassengerDTO;
import airline.tickets.dto.ReservationDTO;
import airline.tickets.exception.BadRequestException;
import airline.tickets.exception.ResourceNotFoundException;
import airline.tickets.model.Flight;
import airline.tickets.model.Passenger;
import airline.tickets.model.Reservation;
import airline.tickets.repository.PassengerRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PassengerService {

    private final PassengerRepository passengerRepository;

    private final ReservationService reservationService;

    private final ConvertModelToDTO convertModelToDTO;

    private static final String NO_PASSENGER_EXIST = "No Passenger found with id: ";

    @RequestCounterAnnotation
    @Cacheable(value = "passengers")
    public List<PassengerDTO> findAllPassengers() {
        List<Passenger> passengerList = passengerRepository.findAll();
        return convertModelToDTO.convertToDTOList(passengerList, convertModelToDTO::passengerConversion);
    }

    @LoggingAnnotation
    public Optional<PassengerDTO> findPassengerById(final Long passengerId) throws ResourceNotFoundException {
        Passenger passenger = passengerRepository.findById(passengerId).
                orElseThrow(() -> new ResourceNotFoundException(NO_PASSENGER_EXIST + passengerId));
        return Optional.of(convertModelToDTO.passengerConversion(passenger));
    }

    public List<PassengerDTO> findPassengersByName(final String name) {
        List<Passenger> passengerList = passengerRepository.findByName(name);
        return convertModelToDTO.convertToDTOList(passengerList, convertModelToDTO::passengerConversion);
    }

    @LoggingAnnotation
    public List<FlightDTO> findAllPassengerFlights(final Long passengerId) throws ResourceNotFoundException {
        Passenger passenger = passengerRepository.findById(passengerId).
                orElseThrow(() -> new ResourceNotFoundException(NO_PASSENGER_EXIST + passengerId));
        List<Flight> flightList = passenger.getFlights();
        return convertModelToDTO.convertToDTOList(flightList, convertModelToDTO::flightConversion);
    }

    @LoggingAnnotation
    public List<ReservationDTO> findAllPassengerReservations(final Long passengerId) throws ResourceNotFoundException {
        Passenger passenger = passengerRepository.findById(passengerId).
                orElseThrow(() -> new ResourceNotFoundException(NO_PASSENGER_EXIST + passengerId));
        List<Reservation> reservationList = passenger.getReservations();
        return convertModelToDTO.convertToDTOList(reservationList, convertModelToDTO::reservationConversion);
    }

    @LoggingAnnotation
    @CacheEvict(value = {"passengers", "reservations"}, allEntries = true)
    public PassengerDTO saveOrUpdatePassenger(final Passenger passenger) throws BadRequestException {
        if (passenger.getName() == null || passenger.getPassportNumber() == null) {
            throw new BadRequestException("name and passportNumber must be provided");
        }
        passengerRepository.save(passenger);
        return convertModelToDTO.passengerConversion(passenger);
    }

    @LoggingAnnotation
    @CacheEvict(value = {"passengers", "reservations"}, allEntries = true)
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
