package airline.tickets.service.impl;

import airline.tickets.dto.AirlineDTO;
import airline.tickets.dto.FlightDTO;
import airline.tickets.dto.PassengerDTO;
import airline.tickets.dto.ReservationDTO;
import airline.tickets.dto.TicketDTO;
import airline.tickets.model.Airline;
import airline.tickets.model.Flight;
import airline.tickets.model.Passenger;
import airline.tickets.model.Reservation;
import airline.tickets.model.Ticket;
import airline.tickets.service.ConvertModelToDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class ConvertModelToDTOImpl implements ConvertModelToDTO {

    @Override
    public AirlineDTO airlineConversion(final Airline airline) {
        AirlineDTO airlineDTO = new AirlineDTO();
        airlineDTO.setId(airline.getId());
        airlineDTO.setName(airline.getName());
        return airlineDTO;
    }

    @Override
    public FlightDTO flightConversion(final Flight flight) {
        FlightDTO flightDTO = new FlightDTO();
        flightDTO.setId(flight.getId());
        flightDTO.setDepartureTown(flight.getDepartureTown());
        flightDTO.setArrivalTown(flight.getArrivalTown());
        flightDTO.setDepartureDateTime(flight.getDepartureDateTime());
        flightDTO.setAirlineName(flight.getAirline().getName());
        return flightDTO;
    }

    @Override
    public PassengerDTO passengerConversion(final Passenger passenger) {
        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setId(passenger.getId());
        passengerDTO.setName(passenger.getName());
        passengerDTO.setPassportNumber(passenger.getPassportNumber());
        return passengerDTO;
    }

    @Override
    public ReservationDTO reservationConversion(final Reservation reservation) {
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setId(reservation.getId());

        PassengerDTO passengerDTO = passengerConversion(reservation.getPassenger());
        TicketDTO ticketDTO = ticketConversion(reservation.getTicket());

        reservationDTO.setPassenger(passengerDTO);
        reservationDTO.setTicket(ticketDTO);
        return reservationDTO;
    }

    @Override
    public TicketDTO ticketConversion(final Ticket ticket) {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(ticket.getId());
        ticketDTO.setPrice(ticket.getPrice());
        ticketDTO.setReserved(ticket.isReserved());

        FlightDTO flightDTO = flightConversion(ticket.getFlight());
        ticketDTO.setFlight(flightDTO);
        return ticketDTO;
    }

    @Override
    public <T, R> List<R> convertToDTOList(final List<T> entities, final Function<T, R> conversionFunction) {
        return entities.stream()
                .map(conversionFunction)
                .toList();
    }
}
