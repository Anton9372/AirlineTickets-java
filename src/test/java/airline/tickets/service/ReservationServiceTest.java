package airline.tickets.service;

import airline.tickets.dto.ReservationDTO;
import airline.tickets.exception.ResourceNotFoundException;
import airline.tickets.model.Airline;
import airline.tickets.model.Flight;
import airline.tickets.model.Passenger;
import airline.tickets.model.Reservation;
import airline.tickets.model.Ticket;
import airline.tickets.repository.FlightRepository;
import airline.tickets.repository.PassengerRepository;
import airline.tickets.repository.ReservationRepository;
import airline.tickets.repository.TicketRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    @Mock
    private FlightRepository flightRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private PassengerRepository passengerRepository;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private ConvertModelToDTO convertModelToDTO;

    @InjectMocks
    private ReservationService reservationService;

    private static final ConvertModelToDTO notMockConvertModelToDTO = new ConvertModelToDTO();

    private static List<Reservation> reservationList;
    private static List<ReservationDTO> reservationDTOList;
    private static Passenger passenger;
    private static Ticket ticket;
    private static Reservation reservation;
    private static ReservationDTO reservationDTO;

    private static final Long passengerId = 5L;
    private static final Long ticketId = 50L;
    private static final Long reservationId = 500L;
    private static final int NUM_OF_REPEATS = 5;

    @BeforeEach
    void set() {

    }

    @BeforeAll
    static void setUp() {

        Airline airline = new Airline();
        airline.setId(10L);
        airline.setName("Airline name");

        passenger = new Passenger();
        passenger.setId(10L);
        passenger.setName("Name");
        passenger.setPassportNumber("ABC");

        Flight flight = new Flight();
        flight.setId(1L);
        flight.setDepartureTown("Minsk");
        flight.setArrivalTown("London");
        flight.setDepartureDateTime(LocalDateTime.of(2024, 4, 1, 0, 0));
        flight.setAirline(airline);

        ticket = new Ticket();
        ticket.setId(50L);
        ticket.setPrice(333L);
        ticket.setReserved(false);
        ticket.setFlight(flight);

        reservation = new Reservation();
        reservation.setId(500L);
        reservation.setPassenger(passenger);
        reservation.setTicket(ticket);

        reservationDTO = notMockConvertModelToDTO.reservationConversion(reservation);

        reservationList = new ArrayList<>();
        for (int i = 0; i < NUM_OF_REPEATS; i++) {
            Reservation reservation = new Reservation();
            reservation.setId((long) i);
            reservation.setPassenger(passenger);
            reservation.setTicket(ticket);
            reservationList.add(reservation);
        }

        passenger.setReservations(reservationList);

        reservationDTOList = notMockConvertModelToDTO.convertToDTOList(reservationList,
                notMockConvertModelToDTO::reservationConversion);
    }


    @Test
    void testFindAllReservations_Valid() {
        when(reservationRepository.findAll()).thenReturn(reservationList);
        doReturn(reservationDTOList).when(convertModelToDTO).convertToDTOList(eq(reservationList), any());

        List<ReservationDTO> result = reservationService.findAllReservations();

        assertEquals(NUM_OF_REPEATS, result.size());
        for (int i = 0; i < NUM_OF_REPEATS; i++) {
            assertEquals(i, result.get(i).getId());
        }
    }

    @Test
    void testFindAllReservations_NoReservationsExist() {
        when(reservationRepository.findAll()).thenReturn(new ArrayList<>());

        List<ReservationDTO> result = reservationService.findAllReservations();

        assertEquals(0, result.size());
    }

    @Test
    void testFindReservationByPassengerId_Valid() {
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(passenger));
        doReturn(reservationDTOList).when(convertModelToDTO).convertToDTOList(eq(reservationList), any());

        List<ReservationDTO> result = reservationService.findReservationsByPassengerId(passengerId);

        assertEquals(NUM_OF_REPEATS, result.size());
        for (int i = 0; i < NUM_OF_REPEATS; i++) {
            assertEquals(i, result.get(i).getId());
        }
    }

    @Test
    void testFindReservationsByPassengerId_NoPassengerExists() {
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> reservationService.findReservationsByPassengerId(passengerId));
    }

    @Test
    void testFindReservationByTicketId_Valid() {
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(reservationRepository.findByTicketId(ticketId)).thenReturn(Optional.of(reservation));
        when(convertModelToDTO.reservationConversion(reservation)).thenReturn(reservationDTO);

        Optional<ReservationDTO> result = reservationService.findReservationByTicketId(ticketId);

        assertTrue(result.isPresent());
        assertEquals(result.get().getId(), reservation.getId());
    }

    @Test
    void testFindReservationByTicketId_NoTicketExists() {
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> reservationService.findReservationByTicketId(ticketId));
    }

    @Test
    void testFindReservationByTicketId_NoReservationExists() {
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(reservationRepository.findByTicketId(ticketId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> reservationService.findReservationByTicketId(ticketId));
    }

    @Test
    void testSaveReservation_Valid() {
        //todo
        Airline airline = new Airline();
        airline.setId(101L);
        airline.setName("Airline name1");

        Passenger passenger = new Passenger();
        passenger.setId(101L);
        passenger.setName("Name1");
        passenger.setPassportNumber("ABC1");

        Flight flight = new Flight();
        flight.setId(11L);
        flight.setDepartureTown("Minsk1");
        flight.setArrivalTown("London1");
        flight.setDepartureDateTime(LocalDateTime.of(2024, 4, 1, 0, 0));
        flight.setAirline(airline);

        Ticket ticket = new Ticket();
        ticket.setId(501L);
        ticket.setPrice(333L);
        ticket.setReserved(true);
        ticket.setFlight(flight);

        Reservation reservation = new Reservation();
        //reservation.setId(500L);
        reservation.setPassenger(passenger);
        reservation.setTicket(ticket);
        ReservationDTO reservationDTO1 = notMockConvertModelToDTO.reservationConversion(reservation);

        when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(passenger));
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(convertModelToDTO.reservationConversion(reservation)).thenReturn(reservationDTO1);

        ReservationDTO result = reservationService.saveReservation(passengerId, ticketId);

        verify(ticketRepository, times(1)).save(ticket);
        verify(reservationRepository, times(1)).save(reservation);
        verify(flightRepository, times(1)).save(ticket.getFlight());
        assertTrue(result.getTicket().isReserved());
    }

    @Test
    void testSaveReservation_NoPassengerExists() {
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> reservationService.saveReservation(passengerId, ticketId));
    }

    @Test
    void testSaveReservation_NoTicketExists() {
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(passenger));
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> reservationService.saveReservation(passengerId, ticketId));
    }

    @Test
    void testSaveBulkReservations_NoPassengerExists() {

    }

    @Test
    void testSaveBulkReservations_NoTicketExists() {

    }

    @Test
    void testDeleteReservation_Valid() {

    }

    @Test
    void testDeleteReservation_NoReservationExists() {
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> reservationService.deleteReservation(reservationId));
    }
}
