package airline.tickets.service;

import airline.tickets.dto.FlightDTO;
import airline.tickets.dto.PassengerDTO;
import airline.tickets.dto.TicketDTO;
import airline.tickets.exception.BadRequestException;
import airline.tickets.exception.ResourceNotFoundException;
import airline.tickets.model.Airline;
import airline.tickets.model.Flight;
import airline.tickets.model.Passenger;
import airline.tickets.model.Ticket;
import airline.tickets.repository.AirlineRepository;
import airline.tickets.repository.FlightRepository;
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {
    @Mock
    private ConvertModelToDTO convertModelToDTO;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private AirlineRepository airlineRepository;

    @Mock
    private TicketService ticketService;

    @InjectMocks
    private FlightService flightService;

    private static final ConvertModelToDTO notMockConvertModelToDTO = new ConvertModelToDTO();

    private static List<Flight> flightList;
    private static List<FlightDTO> flightDTOList;
    private static Flight flight;
    private static Airline airline;

    private List<Ticket> ticketList;

    private final String departureTown = "Departure Town";
    private final String arrivalTown = "Arrival Town";
    private final String airlineName = "Airline name";
    private final Long flightId = 1L;

    private static final int NUM_OF_REPEATS = 5;

    @BeforeEach
    void setTicketList() {
        ticketList = new ArrayList<>();
        for(int i = 0; i < NUM_OF_REPEATS; i++) {
            Ticket ticket = new Ticket();
            ticket.setId((long) i);
            ticket.setPrice(300L);
            ticket.setReserved(false);
            ticket.setFlight(flight);
            ticketList.add(ticket);
        }
    }

    @BeforeAll
    static void setUp() {
        airline = new Airline();
        airline.setId(10L);
        airline.setName("Airline name");

        flight = new Flight();
        flight.setId(1L);
        flight.setDepartureTown("Minsk");
        flight.setArrivalTown("London");
        flight.setDepartureDateTime(LocalDateTime.of(2024, 4, 1, 0, 0));
        flight.setAirline(airline);

        flightList = new ArrayList<>();
        for(int i = 0; i < NUM_OF_REPEATS; i++) {
            Flight flight = new Flight();
            flight.setId((long) i);
            flight.setDepartureTown("Departure" + i);
            flight.setArrivalTown("Arrival" + i);
            flight.setDepartureDateTime(LocalDateTime.of(2024, 4, 1, 0, 0));
            flight.setAirline(airline);
            flightList.add(flight);
        }

        flightDTOList = notMockConvertModelToDTO.convertToDTOList(flightList,
                notMockConvertModelToDTO::flightConversion);
    }

    @Test
    void testFindAllFlights_Valid() {
        when(flightRepository.findAll()).thenReturn(flightList);
        doReturn(flightDTOList).when(convertModelToDTO).convertToDTOList(eq(flightList), any());

        List<FlightDTO> result = flightService.findAllFlights();

        assertEquals(NUM_OF_REPEATS, result.size());
        for (int i = 0; i < NUM_OF_REPEATS; i++) {
            assertEquals(i, result.get(i).getId());
            assertEquals("Departure" + i, result.get(i).getDepartureTown());
            assertEquals("Arrival" + i, result.get(i).getArrivalTown());
        }
    }

    @Test
    void testFindAllFlights_NoFlightsExist() {
        when(flightRepository.findAll()).thenReturn(new ArrayList<>());

        List<FlightDTO> result = flightService.findAllFlights();

        assertEquals(0, result.size());
    }

    @Test
    void testFindAllFlightsByDepartureTown_Valid() {
        when(flightRepository.findByDepartureTown(departureTown)).thenReturn(flightList);
        doReturn(flightDTOList).when(convertModelToDTO).convertToDTOList(eq(flightList), any());

        List<FlightDTO> result = flightService.findFlightByDepartureTown(departureTown);

        assertEquals(5, result.size());
        for (int i = 0; i < NUM_OF_REPEATS; i++) {
            assertEquals(i, result.get(i).getId());
            assertEquals("Departure" + i, result.get(i).getDepartureTown());
            assertEquals("Arrival" + i, result.get(i).getArrivalTown());
        }
    }

    @Test
    void testFindAllFlightsByDepartureTown_NoFlightsExist() {
        when(flightRepository.findByDepartureTown(departureTown)).thenReturn(new ArrayList<>());

        List<FlightDTO> result = flightService.findFlightByDepartureTown(departureTown);

        assertEquals(0, result.size());
    }

    @Test
    void testFindAllFlightsByArrivalTown_Valid() {
        when(flightRepository.findByArrivalTown(arrivalTown)).thenReturn(flightList);
        doReturn(flightDTOList).when(convertModelToDTO).convertToDTOList(eq(flightList), any());

        List<FlightDTO> result = flightService.findFlightByArrivalTown(arrivalTown);

        assertEquals(NUM_OF_REPEATS, result.size());
        for (int i = 0; i < NUM_OF_REPEATS; i++) {
            assertEquals(i, result.get(i).getId());
            assertEquals("Departure" + i, result.get(i).getDepartureTown());
            assertEquals("Arrival" + i, result.get(i).getArrivalTown());
        }
    }

    @Test
    void testFindAllFlightsByArrivalTown_NoFlightsExist() {
        when(flightRepository.findByArrivalTown(arrivalTown)).thenReturn(new ArrayList<>());

        List<FlightDTO> result = flightService.findFlightByArrivalTown(arrivalTown);

        assertEquals(0, result.size());
    }

    @Test
    void testFindAllFlightsByDepartureTownAndArrivalTown_Valid() {
        when(flightRepository.findByDepartureTownAndArrivalTown(departureTown, arrivalTown)).thenReturn(flightList);
        doReturn(flightDTOList).when(convertModelToDTO).convertToDTOList(eq(flightList), any());

        List<FlightDTO> result = flightService.findFlightByDepartureTownAndArrivalTown(departureTown, arrivalTown);

        assertEquals(5, result.size());
        for (int i = 0; i < NUM_OF_REPEATS; i++) {
            assertEquals(i, result.get(i).getId());
            assertEquals("Departure" + i, result.get(i).getDepartureTown());
            assertEquals("Arrival" + i, result.get(i).getArrivalTown());
        }
    }

    @Test
    void testFindAllFlightsByDepartureTownAndArrivalTown_NoFlightsExist() {
        when(flightRepository.findByDepartureTownAndArrivalTown(departureTown, arrivalTown)).
                thenReturn(new ArrayList<>());

        List<FlightDTO> result = flightService.findFlightByDepartureTownAndArrivalTown(departureTown, arrivalTown);

        assertEquals(0, result.size());
    }

    @Test
    void testFindAllFlightTickets_Valid() {
        flight.setTickets(ticketList);
        List<TicketDTO> ticketDTOList = notMockConvertModelToDTO.convertToDTOList(ticketList,
                notMockConvertModelToDTO::ticketConversion);
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        doReturn(ticketDTOList).when(convertModelToDTO).convertToDTOList(eq(ticketList), any());

        List<TicketDTO> result = flightService.findAllFlightTickets(flightId);

        assertEquals(ticketDTOList.size(), result.size());
        for (int i = 0; i < NUM_OF_REPEATS; i++) {
            assertEquals(i, result.get(i).getId());
            assertEquals(300L, result.get(i).getPrice());
            assertFalse(result.get(i).isReserved());
        }
    }

    @Test
    void testFindAllFlightTickets_NoFlightExists() {
        when(flightRepository.findById(flightId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> flightService.findAllFlightTickets(flightId));
    }

    @Test
    void testFindAllFlightPassengers_Valid() {
        List<Passenger> passengerList = new ArrayList<>();
        for(int i = 0; i < NUM_OF_REPEATS; i++) {
            Passenger passenger = new Passenger();
            passenger.setId((long) i);
            passenger.setName("Name" + i);
            passenger.setPassportNumber("ABC" + i);
            passengerList.add(passenger);
        }
        flight.setPassengers(passengerList);
        List<PassengerDTO> passengerDTOList = notMockConvertModelToDTO.convertToDTOList(passengerList,
                notMockConvertModelToDTO::passengerConversion);
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        doReturn(passengerDTOList).when(convertModelToDTO).convertToDTOList(eq(passengerList), any());

        List<PassengerDTO> result = flightService.findAllFlightPassengers(flightId);

        assertEquals(passengerDTOList.size(), result.size());
        for (int i = 0; i < 5; i++) {
            assertEquals(i, result.get(i).getId());
            assertEquals("Name" + i, result.get(i).getName());
            assertEquals("ABC" + i, result.get(i).getPassportNumber());
        }
    }

    @Test
    void testFindAllFlightPassengers_NoFlightExists() {
        when(flightRepository.findById(flightId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> flightService.findAllFlightPassengers(flightId));
    }

    @Test
    void testSaveOrUpdateFlight_Valid() {
        when(airlineRepository.findByName(airlineName)).thenReturn(Optional.of(airline));
        when(flightRepository.save(flight)).thenReturn(flight);
        FlightDTO flightDTO = notMockConvertModelToDTO.flightConversion(flight);
        when(convertModelToDTO.flightConversion(flight)).thenReturn(flightDTO);

        FlightDTO result = flightService.saveOrUpdateFlight(flight, airlineName);

        verify(flightRepository, times(1)).save(flight);
        assertEquals(flightDTO, result);
    }

    @Test
    void testSaveOrUpdateFlight_NoAirlineExists() {
        when(airlineRepository.findByName(airlineName)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> flightService.saveOrUpdateFlight(flight, airlineName));
    }

    @Test
    void testSaveOrUpdateFlight_NotValidObject() {
        Flight flight = new Flight();
        when(airlineRepository.findByName(airlineName)).thenReturn(Optional.of(airline));
        assertThrows(BadRequestException.class, () -> flightService.saveOrUpdateFlight(flight, airlineName));
    }

    @Test
    void testDeleteFlight_Valid() {
        flight.setTickets(ticketList);
        when(flightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        when(airlineRepository.save(airline)).thenReturn(airline);

        assertDoesNotThrow(() -> flightService.deleteFlight(flightId));

        verify(ticketService, times(5)).deleteTicket(anyLong());
        verify(airlineRepository, times(1)).save(airline);
        verify(flightRepository, times(1)).delete(flight);
    }

    @Test
    void testDeleteFlight_NoFlightExists() {
        when(flightRepository.findById(flightId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> flightService.deleteFlight(flightId));
    }
}
