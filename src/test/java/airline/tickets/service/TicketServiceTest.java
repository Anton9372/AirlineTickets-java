package airline.tickets.service;

import airline.tickets.dto.ReservationDTO;
import airline.tickets.dto.TicketDTO;
import airline.tickets.exception.BadRequestException;
import airline.tickets.exception.ResourceNotFoundException;
import airline.tickets.model.Airline;
import airline.tickets.model.Flight;
import airline.tickets.model.Ticket;
import airline.tickets.repository.FlightRepository;
import airline.tickets.repository.TicketRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private FlightRepository flightRepository;
    @Mock
    private  ReservationService reservationService;
    @Mock
    private ConvertModelToDTO convertModelToDTO;

    @InjectMocks
    private TicketService ticketService;

    private static final ConvertModelToDTO notMockConvertModelToDTO = new ConvertModelToDTO();

    private static List<Ticket> ticketList;
    private static List<TicketDTO> ticketDTOList;
    private static Flight flight;
    private static Ticket ticket;
    private static TicketDTO ticketDTO;

    private final String departureTown = "Departure town";
    private final String arrivalTown = "Arrival town";
    private static final Long flightId = 1L;
    private static final Long ticketId = 10L;
    private static final int NUM_OF_REPEATS = 5;

    @BeforeAll
    static void setUp() {

        Airline airline = new Airline();
        airline.setId(10L);
        airline.setName("Airline name");

        flight = new Flight();
        flight.setId(flightId);
        flight.setDepartureTown("Minsk");
        flight.setArrivalTown("London");
        flight.setDepartureDateTime(LocalDateTime.of(2024, 4, 1, 0, 0));
        flight.setAirline(airline);

        ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setPrice(444L);
        ticket.setReserved(false);
        ticket.setFlight(flight);

        ticketDTO = notMockConvertModelToDTO.ticketConversion(ticket);

        ticketList = new ArrayList<>();
        for(int i = 0; i < NUM_OF_REPEATS; i++) {
            Ticket ticket = new Ticket();
            ticket.setId((long) i);
            ticket.setPrice(333L);
            ticket.setReserved(false);
            ticket.setFlight(flight);
            ticketList.add(ticket);
        }
        flight.setTickets(ticketList);

        ticketDTOList = notMockConvertModelToDTO.convertToDTOList(ticketList,
                notMockConvertModelToDTO::ticketConversion);
    }

    @Test
    void testFindAllTickets_Valid() {
        when(ticketRepository.findAll()).thenReturn(ticketList);
        doReturn(ticketDTOList).when(convertModelToDTO).convertToDTOList(eq(ticketList), any());

        List<TicketDTO> result = ticketService.findAllTickets();

        assertEquals(NUM_OF_REPEATS, result.size());
        for(int i = 0; i < NUM_OF_REPEATS; i++) {
            assertEquals(i, result.get(i).getId());
            assertEquals(333L, result.get(i).getPrice());
        }
    }

    @Test
    void testFindAllTickets_NoTicketsExist() {
        when(ticketRepository.findAll()).thenReturn(new ArrayList<>());

        List<TicketDTO> result = ticketService.findAllTickets();

        assertEquals(0, result.size());
    }

    @Test
    void testFindAllTicketsByFlightList_Valid() {
        when(convertModelToDTO.ticketConversion(any())).thenReturn(new TicketDTO());

        List<TicketDTO> result = ticketService.findAllTicketsByFlightList(Collections.singletonList(flight));

        assertEquals(NUM_OF_REPEATS, result.size());
    }

    @Test
    void testFindAllTicketsByFlightList_NoTicketsExist() {
        List<TicketDTO> result = ticketService.findAllTicketsByFlightList(Collections.singletonList(new Flight()));

        assertEquals(0, result.size());
    }

    @Test
    void testFindUnreservedTicketsByFlightList_Valid() {
        when(convertModelToDTO.ticketConversion(any())).thenReturn(new TicketDTO());

        List<TicketDTO> result = ticketService.findUnreservedTicketsByFlightList(Collections.singletonList(flight));

        assertEquals(NUM_OF_REPEATS, result.size());
        for(int i = 0; i < NUM_OF_REPEATS; i++) {
            assertFalse(result.get(i).isReserved());
        }
    }

    @Test
    void testFindUnreservedTicketsByFlightList_NoUnreservedTicketsExist() {
        List<TicketDTO> result = ticketService.findUnreservedTicketsByFlightList(Collections.singletonList(new Flight()));

        assertEquals(0, result.size());
    }

    @Test
    void testFindAllTicketsByFlightId_Valid() {
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        when(convertModelToDTO.ticketConversion(any())).thenReturn(new TicketDTO());

        List<TicketDTO> result = ticketService.findAllTicketsByFlightId(flightId);

        assertEquals(NUM_OF_REPEATS, result.size());
    }

    @Test
    void testFindAllTicketsByFlightId_NoFlightExists() {
        when(flightRepository.findById(flightId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> ticketService.findAllTicketsByFlightId(flightId));
    }

    @Test
    void testFindAllTicketsByDepartureTown_Valid() {
        when(flightRepository.findByDepartureTown(departureTown)).thenReturn(Collections.singletonList(flight));
        when(convertModelToDTO.ticketConversion(any())).thenReturn(new TicketDTO());

        List<TicketDTO> result = ticketService.findAllTicketsByDepartureTown(departureTown);

        assertEquals(NUM_OF_REPEATS, result.size());
    }

    @Test
    void testFindAllTicketsByArrivalTown_Valid() {
        when(flightRepository.findByArrivalTown(arrivalTown)).thenReturn(Collections.singletonList(flight));
        when(convertModelToDTO.ticketConversion(any())).thenReturn(new TicketDTO());

        List<TicketDTO> result = ticketService.findAllTicketsByArrivalTown(arrivalTown);

        assertEquals(NUM_OF_REPEATS, result.size());
    }

    @Test
    void testFindAllTicketsByDepartureTownAndArrivalTown_Valid() {
        when(flightRepository.findByDepartureTownAndArrivalTown(departureTown, arrivalTown)).
                thenReturn(Collections.singletonList(flight));
        when(convertModelToDTO.ticketConversion(any())).thenReturn(new TicketDTO());

        List<TicketDTO> result = ticketService.findAllTicketsByDepartureTownAndArrivalTown(departureTown, arrivalTown);

        assertEquals(NUM_OF_REPEATS, result.size());
    }

    @Test
    void testFindUnreservedTicketsByFlightId_Valid() {
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        when(convertModelToDTO.ticketConversion(any())).thenReturn(new TicketDTO());

        List<TicketDTO> result = ticketService.findAllTicketsByFlightId(flightId);

        assertEquals(NUM_OF_REPEATS, result.size());
        for(int i = 0; i < NUM_OF_REPEATS; i++) {
            assertFalse(result.get(i).isReserved());
        }
    }
    @Test
    void testFindUnreservedTicketsByFlightId_NoFlightExists() {
        when(flightRepository.findById(flightId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> ticketService.findAllTicketsByFlightId(flightId));
    }

    @Test
    void testFindUnreservedTicketsByDepartureTown_Valid() {
        when(flightRepository.findByDepartureTown(departureTown)).thenReturn(Collections.singletonList(flight));
        when(convertModelToDTO.ticketConversion(any())).thenReturn(new TicketDTO());

        List<TicketDTO> result = ticketService.findAllTicketsByDepartureTown(departureTown);

        assertEquals(NUM_OF_REPEATS, result.size());
        for(int i = 0; i < NUM_OF_REPEATS; i++) {
            assertFalse(result.get(i).isReserved());
        }
    }

    @Test
    void testFindUnreservedTicketsByArrivalTown_Valid() {
        when(flightRepository.findByArrivalTown(arrivalTown)).thenReturn(Collections.singletonList(flight));
        when(convertModelToDTO.ticketConversion(any())).thenReturn(new TicketDTO());

        List<TicketDTO> result = ticketService.findAllTicketsByArrivalTown(arrivalTown);

        assertEquals(NUM_OF_REPEATS, result.size());
        for(int i = 0; i < NUM_OF_REPEATS; i++) {
            assertFalse(result.get(i).isReserved());
        }
    }

    @Test
    void testFindUnreservedTicketsByDepartureTownAndArrivalTown_Valid() {
        when(flightRepository.findByDepartureTownAndArrivalTown(departureTown, arrivalTown)).
                thenReturn(Collections.singletonList(flight));
        when(convertModelToDTO.ticketConversion(any())).thenReturn(new TicketDTO());

        List<TicketDTO> result = ticketService.findAllTicketsByDepartureTownAndArrivalTown(departureTown, arrivalTown);

        assertEquals(NUM_OF_REPEATS, result.size());
        for(int i = 0; i < NUM_OF_REPEATS; i++) {
            assertFalse(result.get(i).isReserved());
        }
    }

    @Test
    void testSaveOrUpdateTicket_Valid() {
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        when(ticketRepository.save(ticket)).thenReturn(ticket);
        when(convertModelToDTO.ticketConversion(ticket)).thenReturn(ticketDTO);

        TicketDTO result = ticketService.saveOrUpdateTicket(ticket, flightId);

        assertEquals(ticketDTO, result);
        assertEquals(flight.getId(), result.getFlight().getId());
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    void testSaveOrUpdateTicket_NoFlightExists() {
        when(flightRepository.findById(flightId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> ticketService.saveOrUpdateTicket(new Ticket(), flightId));
    }

    @Test
    void testSaveOrUpdateTicket_NotValidTicket() {
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        assertThrows(BadRequestException.class, () -> ticketService.saveOrUpdateTicket(new Ticket(), flightId));
    }

    @Test
    void testSaveNumOfTickets_Valid() {
        ticket.setId(null);
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        when(convertModelToDTO.ticketConversion(ticket)).thenReturn(ticketDTO);

        List<TicketDTO> result = ticketService.saveNumOfTickets(ticket, flightId, NUM_OF_REPEATS);

        assertEquals(NUM_OF_REPEATS, result.size());
        for(int i = 0; i < NUM_OF_REPEATS; i++) {
            assertFalse(result.get(i).isReserved());
            assertEquals(flight.getId(), result.get(i).getFlight().getId());
        }
        verify(ticketRepository, times(NUM_OF_REPEATS)).save(any());
    }

    @Test
    void testSaveNumOfTickets_NoFlightExists() {
        when(flightRepository.findById(flightId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> ticketService.saveNumOfTickets(new Ticket(), flightId,
                NUM_OF_REPEATS));
    }

    @Test
    void testSaveNumOfTickets_NotValidTicket() {
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        assertThrows(BadRequestException.class, () -> ticketService.saveNumOfTickets(new Ticket(), flightId,
                NUM_OF_REPEATS));
    }

    @Test
    void testSaveNumOfTickets_NotValidNumOfTickets() {
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        assertThrows(BadRequestException.class, () -> ticketService.saveNumOfTickets(ticket, flightId, 0));
    }

    @Test
    void testDeleteTicket_Unreserved() {
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));

        ticketService.deleteTicket(ticketId);

        verify(ticketRepository, times(1)).delete(ticket);
        verify(reservationService, never()).deleteReservation(anyLong());
    }

    @Test
    void testDeleteTicket_Reserved() {
        ticket.setReserved(true);
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setId(10L);
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(reservationService.findReservationByTicketId(ticketId)).thenReturn(Optional.of(reservationDTO));

        ticketService.deleteTicket(ticketId);

        verify(ticketRepository, times(1)).delete(ticket);
        verify(reservationService, times(1)).findReservationByTicketId(ticketId);
        verify(reservationService, times(1)).deleteReservation(10L);
    }

    @Test
    void testDeleteTicket_NoTicketExists() {
        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> ticketService.deleteTicket(ticketId));
    }
}
