package airline.tickets.service;

import airline.tickets.aspect.AspectAnnotation;
import airline.tickets.dto.ReservationDTO;
import airline.tickets.exception.ResourceNotFoundException;
import airline.tickets.model.Flight;
import airline.tickets.model.Passenger;
import airline.tickets.model.Reservation;
import airline.tickets.model.Ticket;
import airline.tickets.repository.FlightRepository;
import airline.tickets.repository.PassengerRepository;
import airline.tickets.repository.ReservationRepository;
import airline.tickets.repository.TicketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservationService {

    private final FlightRepository flightRepository;
    private final ReservationRepository reservationRepository;
    private final PassengerRepository passengerRepository;
    private final TicketRepository ticketRepository;

    private final ConvertModelToDTO convertModelToDTO;

    private static final String NO_PASSENGER_EXIST = "No Passenger found with id: ";
    private static final String NO_TICKET_EXIST = "No Ticket found with id: ";
    private static final String NO_RESERVATION_EXIST = "No Reservation found with id: ";

    public List<ReservationDTO> findAllReservations() {
        List<Reservation> reservationList = reservationRepository.findAll();
        return convertModelToDTO.convertToDTOList(reservationList, convertModelToDTO::reservationConversion);
    }

    @AspectAnnotation
    public List<ReservationDTO> findReservationByPassengerId(final Long passengerId) throws ResourceNotFoundException {
        Passenger passenger = passengerRepository.findById(passengerId).
                orElseThrow(() -> new ResourceNotFoundException(NO_PASSENGER_EXIST + passengerId));
        List<Reservation> reservationList = passenger.getReservations();
        return convertModelToDTO.convertToDTOList(reservationList, convertModelToDTO::reservationConversion);
    }

    @AspectAnnotation
    public Optional<ReservationDTO> findReservationByTicketId(final Long ticketId) throws ResourceNotFoundException {
        Ticket ticket = ticketRepository.findById(ticketId).
                orElseThrow(() -> new ResourceNotFoundException(NO_TICKET_EXIST + ticketId));

        Optional<Reservation> optionalReservation = reservationRepository.findByTicketId(ticket.getId());
        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            return Optional.of(convertModelToDTO.reservationConversion(reservation));
        } else {
            return Optional.empty();
        }
    }

    @AspectAnnotation
    public ReservationDTO saveReservation(final Long passengerId, final Long ticketId)
            throws ResourceNotFoundException {
        Passenger passenger = passengerRepository.findById(passengerId).
                orElseThrow(() -> new ResourceNotFoundException(NO_PASSENGER_EXIST + passengerId));
        Ticket ticket = ticketRepository.findById(ticketId).
                orElseThrow(() -> new ResourceNotFoundException(NO_TICKET_EXIST + ticketId));

        Flight flight = ticket.getFlight();

        ticket.setReserved(true);
        ticketRepository.save(ticket);

        Reservation reservation = new Reservation();
        reservation.setTicket(ticket);
        reservation.setPassenger(passenger);
        reservationRepository.save(reservation);

        List<Passenger> passengerList = flight.getPassengers();
        passengerList.add(passenger);
        flight.setPassengers(passengerList);
        flightRepository.save(flight);

        return convertModelToDTO.reservationConversion(reservation);
    }

    @AspectAnnotation
    public List<ReservationDTO> saveBulkReservations(Long passengerId, List<Long> ticketIds)
            throws ResourceNotFoundException {
        return ticketIds.stream()
                .map(ticketId -> saveReservation(passengerId, ticketId))
                .toList();
    }

    @AspectAnnotation
    public void deleteReservation(final Long reservationId) throws ResourceNotFoundException {
        Reservation reservation = reservationRepository.findById(reservationId).
                orElseThrow(() -> new ResourceNotFoundException(NO_RESERVATION_EXIST + reservationId));

        Ticket ticket = reservation.getTicket();
        Passenger passenger = reservation.getPassenger();
        Flight flight = ticket.getFlight();

        ticket.setReserved(false);
        ticketRepository.save(ticket);

        List<Reservation> reservationList = passenger.getReservations();
        reservationList.remove(reservation);
        passenger.setReservations(reservationList);
        passengerRepository.save(passenger);

        List<Passenger> passengerList = flight.getPassengers();
        passengerList.remove(passenger);
        flight.setPassengers(passengerList);
        flightRepository.save(flight);

        reservationRepository.delete(reservation);
    }
}
