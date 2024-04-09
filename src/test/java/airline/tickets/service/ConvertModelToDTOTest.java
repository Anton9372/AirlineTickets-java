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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ConvertModelToDTOTest {

    @InjectMocks
    private ConvertModelToDTO convertModelToDTO;

    private static Airline airline;
    private static Flight flight;
    private static Passenger passenger;
    private static Reservation reservation;
    private static Ticket ticket;

    @BeforeAll
    static void setUp() {
        airline = new Airline();
        airline.setId(1L);
        airline.setName("Airline name");

        flight = new Flight();
        flight.setId(10L);
        flight.setDepartureTown("Departure town");
        flight.setArrivalTown("Arrival town");
        flight.setDepartureDateTime(LocalDateTime.of(2024, 4, 1, 0, 0));
        flight.setAirline(airline);

        passenger = new Passenger();
        passenger.setId(100L);
        passenger.setName("Passenger name");
        passenger.setPassportNumber("AB123456");

        ticket = new Ticket();
        ticket.setId(1000L);
        ticket.setPrice(300L);
        ticket.setReserved(true);
        ticket.setFlight(flight);

        reservation = new Reservation();
        reservation.setId(10000L);
        reservation.setPassenger(passenger);
        reservation.setTicket(ticket);
    }

    @Test
    void testAirlineConversion_Valid() {
        AirlineDTO airlineDTO = convertModelToDTO.airlineConversion(airline);

        assertEquals(airline.getId(), airlineDTO.getId());
        assertEquals(airline.getName(), airlineDTO.getName());
    }

    @Test
    void testAirlineConversion_NotValidObject() {
        assertThrows(BadRequestException.class, () -> convertModelToDTO.airlineConversion(null));
    }

    @Test
    void testFlightConversion_Valid() {
        FlightDTO flightDTO = convertModelToDTO.flightConversion(flight);

        assertEquals(flight.getId(), flightDTO.getId());
        assertEquals(flight.getDepartureTown(), flightDTO.getDepartureTown());
        assertEquals(flight.getArrivalTown(), flightDTO.getArrivalTown());
        assertEquals(flight.getDepartureDateTime(), flightDTO.getDepartureDateTime());
        assertEquals(airline.getName(), flightDTO.getAirlineName());
    }

    @Test
    void testFlightConversion_NotValidObject() {
        assertThrows(BadRequestException.class, () -> convertModelToDTO.flightConversion(null));
    }

    @Test
    void testPassengerConversion_Valid() {
        PassengerDTO passengerDTO = convertModelToDTO.passengerConversion(passenger);

        assertEquals(passenger.getId(), passengerDTO.getId());
        assertEquals(passenger.getName(), passengerDTO.getName());
        assertEquals(passenger.getPassportNumber(), passengerDTO.getPassportNumber());
    }

    @Test
    void testPassengerConversion_NotValidObject() {
        assertThrows(BadRequestException.class, () -> convertModelToDTO.passengerConversion(null));
    }

    @Test
    void testReservationConversion() {
        ReservationDTO reservationDTO = convertModelToDTO.reservationConversion(reservation);

        assertEquals(reservation.getId(), reservationDTO.getId());
        assertEquals(passenger.getId(), reservationDTO.getPassenger().getId());
        assertEquals(passenger.getName(), reservationDTO.getPassenger().getName());
        assertEquals(passenger.getPassportNumber(), reservationDTO.getPassenger().getPassportNumber());
        assertEquals(ticket.getId(), reservationDTO.getTicket().getId());
        assertEquals(ticket.getPrice(), reservationDTO.getTicket().getPrice());
        assertEquals(ticket.isReserved(), reservationDTO.getTicket().isReserved());
    }

    @Test
    void testReservationConversion_NotValidObject() {
        assertThrows(BadRequestException.class, () -> convertModelToDTO.reservationConversion(null));
    }

    @Test
    void testTicketConversion_Valid() {
        TicketDTO ticketDTO = convertModelToDTO.ticketConversion(ticket);

        assertEquals(ticket.getId(), ticketDTO.getId());
        assertEquals(ticket.getPrice(), ticketDTO.getPrice());
        assertEquals(ticket.isReserved(), ticketDTO.isReserved());
        assertEquals(flight.getId(), ticketDTO.getFlight().getId());
        assertEquals(flight.getDepartureTown(), ticketDTO.getFlight().getDepartureTown());
        assertEquals(flight.getArrivalTown(), ticketDTO.getFlight().getArrivalTown());
        assertEquals(flight.getDepartureDateTime(), ticketDTO.getFlight().getDepartureDateTime());
    }

    @Test
    void testTicketConversion_NotValidObject() {
        assertThrows(BadRequestException.class, () -> convertModelToDTO.ticketConversion(null));
    }

    @Test
    void testConvertToDTOList_Valid() {
        List<Airline> airlineList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Airline airline = new Airline();
            airline.setId((long) i);
            airline.setName("Airline" + i);
            airlineList.add(airline);
        }

        List<AirlineDTO> airlineDTOList = convertModelToDTO.convertToDTOList(airlineList,
                convertModelToDTO::airlineConversion);

        assertEquals(airlineList.size(), airlineDTOList.size());
        for (int i = 0; i < airlineList.size(); i++) {
            assertEquals(airlineList.get(i).getId(), airlineDTOList.get(i).getId());
            assertEquals(airlineList.get(i).getName(), airlineDTOList.get(i).getName());
        }
    }

    @Test
    void testConvertToDTOList_EmptyList() {
        List<Airline> airlineList = new ArrayList<>();

        List<AirlineDTO> airlineDTOList = convertModelToDTO.convertToDTOList(airlineList,
                convertModelToDTO::airlineConversion);

        assertNotNull(airlineDTOList);
        assertTrue(airlineDTOList.isEmpty());
    }
}