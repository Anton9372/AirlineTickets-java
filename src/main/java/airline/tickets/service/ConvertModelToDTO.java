package airline.tickets.service;

import airline.tickets.dto.AirlineDTO;
import airline.tickets.dto.FlightDTO;
import airline.tickets.dto.PassengerDTO;
import airline.tickets.dto.ReservationDTO;
import airline.tickets.dto.TicketDTO;
import airline.tickets.exception.BadRequestException;
import airline.tickets.model.Airline;
import airline.tickets.model.Flight;
import airline.tickets.model.Passenger;
import airline.tickets.model.Reservation;
import airline.tickets.model.Ticket;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class ConvertModelToDTO {

    private static final String NOT_VALID_ARGUMENT = "Null object to convert: ";

    public AirlineDTO airlineConversion(final Airline airline) throws BadRequestException {
        if (airline == null) {
            throw new BadRequestException(NOT_VALID_ARGUMENT + "airline");
        }
        AirlineDTO airlineDTO = new AirlineDTO();
        airlineDTO.setId(airline.getId());
        airlineDTO.setName(airline.getName());
        return airlineDTO;
    }

    public FlightDTO flightConversion(final Flight flight) throws BadRequestException {
        if (flight == null) {
            throw new BadRequestException(NOT_VALID_ARGUMENT + "flight");
        }
        FlightDTO flightDTO = new FlightDTO();
        flightDTO.setId(flight.getId());
        flightDTO.setDepartureTown(flight.getDepartureTown());
        flightDTO.setArrivalTown(flight.getArrivalTown());
        flightDTO.setDepartureDateTime(flight.getDepartureDateTime());

        AirlineDTO airlineDTO = airlineConversion(flight.getAirline());
        flightDTO.setAirline(airlineDTO);
        return flightDTO;
    }

    public PassengerDTO passengerConversion(final Passenger passenger) throws BadRequestException {
        if (passenger == null) {
            throw new BadRequestException(NOT_VALID_ARGUMENT + "passenger");
        }
        PassengerDTO passengerDTO = new PassengerDTO();
        passengerDTO.setId(passenger.getId());
        passengerDTO.setName(passenger.getName());
        passengerDTO.setPassportNumber(passenger.getPassportNumber());
        return passengerDTO;
    }

    public ReservationDTO reservationConversion(final Reservation reservation) throws BadRequestException {
        if (reservation == null) {
            throw new BadRequestException(NOT_VALID_ARGUMENT + "reservation");
        }
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setId(reservation.getId());

        PassengerDTO passengerDTO = passengerConversion(reservation.getPassenger());
        TicketDTO ticketDTO = ticketConversion(reservation.getTicket());

        reservationDTO.setPassenger(passengerDTO);
        reservationDTO.setTicket(ticketDTO);
        return reservationDTO;
    }

    public TicketDTO ticketConversion(final Ticket ticket) throws BadRequestException {
        if (ticket == null) {
            throw new BadRequestException(NOT_VALID_ARGUMENT + "ticket");
        }
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(ticket.getId());
        ticketDTO.setPrice(ticket.getPrice());
        ticketDTO.setReserved(ticket.isReserved());

        FlightDTO flightDTO = flightConversion(ticket.getFlight());
        ticketDTO.setFlight(flightDTO);
        return ticketDTO;
    }

    public <T, R> List<R> convertToDTOList(final List<T> entities, final Function<T, R> conversionFunction) {
        return entities.stream()
                .map(conversionFunction)
                .toList();
    }
}
