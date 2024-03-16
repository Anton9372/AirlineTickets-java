package airline.tickets.service.impl;

import airline.tickets.dto.ReservationDTO;
import airline.tickets.model.Flight;
import airline.tickets.model.Passenger;
import airline.tickets.model.Reservation;
import airline.tickets.model.Ticket;
import airline.tickets.repository.FlightRepository;
import airline.tickets.repository.PassengerRepository;
import airline.tickets.repository.ReservationRepository;
import airline.tickets.repository.TicketRepository;
import airline.tickets.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final FlightRepository flightRepository;
    private final ReservationRepository reservationRepository;
    private final PassengerRepository passengerRepository;
    private final TicketRepository ticketRepository;
    private final ConvertModelToDTOImpl convertModelToDTO;

    @Override
    public List<ReservationDTO> findAllReservations() {
        List<Reservation> reservationList = reservationRepository.findAll();
        return convertModelToDTO.convertToDTOList(reservationList, convertModelToDTO::reservationConversion);
    }

    @Override
    public List<ReservationDTO> findByPassengerId(Long passengerId) {
        List<Reservation> reservationList = reservationRepository.findByPassengerId(passengerId);
        return convertModelToDTO.convertToDTOList(reservationList, convertModelToDTO::reservationConversion);
    }

    @Override
    public Optional<ReservationDTO> findByTicketId(Long ticketId) {
        Optional<Reservation> optionalReservation = reservationRepository.findByTicketId(ticketId);
        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
            return Optional.of(convertModelToDTO.reservationConversion(reservation));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public ReservationDTO saveReservation(Long passengerId, Long ticketId) {
        Optional<Passenger> optionalPassenger = passengerRepository.findById(passengerId);
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
        if (optionalPassenger.isPresent() && optionalTicket.isPresent()) {
            Passenger passenger = optionalPassenger.get();
            Ticket ticket = optionalTicket.get();
            Flight flight = ticket.getFlight();

            ticket.setReserved(true);

            Reservation reservation = new Reservation();
            reservation.setTicket(ticket);
            reservation.setPassenger(passenger);
            reservationRepository.save(reservation);

            List<Passenger> passengerList = flight.getPassengers();
            passengerList.add(passenger);
            flight.setPassengers(passengerList);
            flightRepository.save(flight);

            return convertModelToDTO.reservationConversion(reservation);
        } else {
            return null;
        }
    }

    @Override
    public void deleteReservation(Long reservationId) {
        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
        if (optionalReservation.isPresent()) {
            Reservation reservation = optionalReservation.get();
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
}
