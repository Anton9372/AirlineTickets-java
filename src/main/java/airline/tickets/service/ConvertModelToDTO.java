package airline.tickets.service;

import airline.tickets.dto.*;
import airline.tickets.model.*;

import java.util.List;
import java.util.function.Function;

public interface ConvertModelToDTO {

    AirlineDTO airlineConversion(Airline airline);

    FlightDTO flightConversion(Flight flight);

    PassengerDTO passengerConversion(Passenger passenger);

    ReservationDTO reservationConversion(Reservation reservation);

    TicketDTO ticketConversion(Ticket ticket);

    <T, R> List<R> convertToDTOList(List<T> entities, Function<T, R> conversionFunction);
}
