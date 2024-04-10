package airline.tickets.service;

import airline.tickets.dto.TicketDTO;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
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

    private static List<Ticket> ticketList1;

    private static List<Ticket> ticketList2;

    private static List<TicketDTO> ticketDTOList1;

    private static List<TicketDTO> ticketDTOList2;

    private static Flight flight1;

    private static Flight flight2;

    private final String departureTown = "Departure town";
    private final String arrivalTown = "Arrival town";

    //private final Long flightId

    private static final int NUM_OF_REPEATS = 5;

    @BeforeAll
    static void setUp() {

        Airline airline = new Airline();
        airline.setId(10L);
        airline.setName("Airline name");

        flight1 = new Flight();
        flight1.setId(1L);
        flight1.setDepartureTown("Minsk");
        flight1.setArrivalTown("London");
        flight1.setDepartureDateTime(LocalDateTime.of(2024, 4, 1, 0, 0));
        flight1.setAirline(airline);

        flight2 = new Flight();
        flight2.setId(1L);
        flight2.setDepartureTown("Paris");
        flight2.setArrivalTown("Moscow");
        flight2.setDepartureDateTime(LocalDateTime.of(2024, 10, 4, 0, 0));
        flight2.setAirline(airline);


        ticketList1 = new ArrayList<>();
        for(int i = 0; i < NUM_OF_REPEATS; i++) {
            Ticket ticket = new Ticket();
            ticket.setId((long) i);
            ticket.setPrice(333L);
            ticket.setReserved(false);
            ticket.setFlight(flight1);
            ticketList1.add(ticket);
        }
        flight1.setTickets(ticketList1);

        ticketList2 = new ArrayList<>();
        for(int i = NUM_OF_REPEATS; i < 2 * NUM_OF_REPEATS; i++) {
            Ticket ticket = new Ticket();
            ticket.setId((long) i);
            ticket.setPrice(228L);
            ticket.setReserved(false);
            ticket.setFlight(flight2);
            ticketList2.add(ticket);
        }
        flight2.setTickets(ticketList2);

        ticketDTOList1 = notMockConvertModelToDTO.convertToDTOList(ticketList1,
                notMockConvertModelToDTO::ticketConversion);

        ticketDTOList2 = notMockConvertModelToDTO.convertToDTOList(ticketList2,
                notMockConvertModelToDTO::ticketConversion);
    }

    @Test
    void testFindAllTickets_Valid() {
        when(ticketRepository.findAll()).thenReturn(ticketList1);
        doReturn(ticketDTOList1).when(convertModelToDTO).convertToDTOList(eq(ticketList1), any());

        List<TicketDTO> result = ticketService.findAllTickets();

        assertEquals(NUM_OF_REPEATS, result.size());
        for(int i = 0; i < NUM_OF_REPEATS; i++) {
            assertEquals(i, result.get(i).getId());
            assertEquals(333L, result.get(i).getPrice());
            assertFalse(result.get(i).isReserved());
        }
    }

    @Test
    void testFindAllTickets_NoTicketsExist() {
        when(ticketRepository.findAll()).thenReturn(new ArrayList<>());

        List<TicketDTO> result = ticketService.findAllTickets();

        assertEquals(0, result.size());
    }

    //todo
    //2 methods

    @Test
    void testFindAllTicketsByFlightId_Valid() {

    }

    @Test
    void testFindAllTicketsByDepartureTown_Valid() {

    }
}
