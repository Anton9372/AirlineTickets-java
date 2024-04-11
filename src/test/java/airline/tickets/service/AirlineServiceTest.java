package airline.tickets.service;

import airline.tickets.dto.AirlineDTO;
import airline.tickets.dto.FlightDTO;
import airline.tickets.exception.BadRequestException;
import airline.tickets.exception.ResourceNotFoundException;
import airline.tickets.model.Airline;
import airline.tickets.model.Flight;
import airline.tickets.repository.AirlineRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AirlineServiceTest {

    @Mock
    private ConvertModelToDTO convertModelToDTO;

    @Mock
    private AirlineRepository airlineRepository;

    @Mock
    private FlightService flightService;

    @InjectMocks
    private AirlineService airlineService;

    private static final ConvertModelToDTO notMockConvertModelToDTO = new ConvertModelToDTO();

    private static Airline airline;
    private static AirlineDTO airlineDTO;

    private final String airlineName = "Airline name";

    private static final int NUM_OF_REPEATS = 5;

    @BeforeAll
    static void setUp() {
        airline = new Airline();
        airline.setId(1L);
        airline.setName("Airline name");
        List<Flight> flightList = new ArrayList<>();
        for (int i = 0; i < NUM_OF_REPEATS; i++) {
            Flight flight = new Flight();
            flight.setId((long) i);
            flight.setDepartureTown("Departure" + i);
            flight.setArrivalTown("Arrival" + i);
            flight.setDepartureDateTime(LocalDateTime.of(2024, 4, 1, 0, 0));
            flight.setAirline(airline);
            flightList.add(flight);
        }
        airline.setFlights(flightList);
        airlineDTO = notMockConvertModelToDTO.airlineConversion(airline);
    }

    @Test
    void testFindAllAirlines_Valid() {
        List<Airline> airlineList = new ArrayList<>();
        for (int i = 0; i < NUM_OF_REPEATS; i++) {
            Airline airline = new Airline();
            airline.setId((long) i);
            airline.setName("Airline" + i);
            airlineList.add(airline);
        }
        List<AirlineDTO> airlineDTOList = notMockConvertModelToDTO.convertToDTOList(airlineList,
                notMockConvertModelToDTO::airlineConversion);
        when(airlineRepository.findAll()).thenReturn(airlineList);
        doReturn(airlineDTOList).when(convertModelToDTO).convertToDTOList(eq(airlineList), any());

        List<AirlineDTO> result = airlineService.findAllAirlines();

        assertEquals(NUM_OF_REPEATS, result.size());
        for (int i = 0; i < NUM_OF_REPEATS; i++) {
            assertEquals(i, result.get(i).getId());
            assertEquals("Airline" + i, result.get(i).getName());
        }
    }

    @Test
    void testFindAllAirlines_NoAirlinesExist() {
        when(airlineRepository.findAll()).thenReturn(new ArrayList<>());

        List<AirlineDTO> result = airlineService.findAllAirlines();

        assertEquals(0, result.size());
    }

    @Test
    void testFindAirlineByName_Valid() {
        when(airlineRepository.findByName(airlineName)).thenReturn(Optional.of(airline));
        when(convertModelToDTO.airlineConversion(airline)).thenReturn(airlineDTO);

        Optional<AirlineDTO> result = airlineService.findAirlineByName(airlineName);

        assertTrue(result.isPresent());
        assertEquals(airlineDTO, result.get());
    }

    @Test
    void testFindAllAirlineFlights_Valid() {
        when(airlineRepository.findByName(airlineName)).thenReturn(Optional.of(airline));
        List<FlightDTO> expectedFlightDTOList = notMockConvertModelToDTO.convertToDTOList(airline.getFlights(),
                notMockConvertModelToDTO::flightConversion);
        doReturn(expectedFlightDTOList).when(convertModelToDTO).convertToDTOList(eq(airline.getFlights()), any());

        List<FlightDTO> result = airlineService.findAllAirlineFlights(airlineName);

        assertEquals(expectedFlightDTOList.size(), result.size());
        for (int i = 0; i < expectedFlightDTOList.size(); i++) {
            assertEquals(expectedFlightDTOList.get(i), result.get(i));
        }
    }

    @Test
    void testSaveOrUpdateAirline_Valid() {
        when(airlineRepository.save(airline)).thenReturn(airline);
        when(convertModelToDTO.airlineConversion(airline)).thenReturn(airlineDTO);

        AirlineDTO resultAirlineDTO = airlineService.saveOrUpdateAirline(airline);

        verify(airlineRepository, times(1)).save(airline);
        assertEquals(airlineDTO, resultAirlineDTO);
    }

    @Test
    void testSaveOrUpdateAirline_NotValidObject() {
        Airline airline = new Airline();
        assertThrows(BadRequestException.class, () -> airlineService.saveOrUpdateAirline(airline));
    }

    @Test
    void testDeleteAirline_Valid() {
        when(airlineRepository.findByName(airlineName)).thenReturn(Optional.of(airline));

        assertDoesNotThrow(() -> airlineService.deleteAirline(airlineName));

        verify(flightService, times(NUM_OF_REPEATS)).deleteFlight(anyLong());
        verify(airlineRepository, times(1)).delete(airline);
    }

    @ParameterizedTest
    @ValueSource(strings = {"findAirlineByName", "findAllAirlineFlights", "deleteAirline"})
    void testNoAirlineExists(String methodName) {
        when(airlineRepository.findByName(airlineName)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            switch (methodName) {
                case "findAirlineByName": {
                    airlineService.findAirlineByName(airlineName);
                    break;
                }
                case "findAllAirlineFlights": {
                    airlineService.findAllAirlineFlights(airlineName);
                    break;
                }
                case "deleteAirline": {
                    airlineService.deleteAirline(airlineName);
                    break;
                }
            }
        });
    }
}
