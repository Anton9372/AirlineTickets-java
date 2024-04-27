package airline.tickets.service;

import airline.tickets.dto.FlightDTO;
import airline.tickets.dto.PassengerDTO;
import airline.tickets.dto.ReservationDTO;
import airline.tickets.exception.BadRequestException;
import airline.tickets.exception.ResourceNotFoundException;
import airline.tickets.model.Airline;
import airline.tickets.model.Flight;
import airline.tickets.model.Passenger;
import airline.tickets.model.Reservation;
import airline.tickets.model.Ticket;
import airline.tickets.repository.PassengerRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
class PassengerServiceTest {

    @Mock
    private ConvertModelToDTO convertModelToDTO;

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private PassengerService passengerService;

    private static final ConvertModelToDTO notMockConvertModelToDTO = new ConvertModelToDTO();

    private static Passenger passenger;
    private static List<Flight> flightList;
    private static List<Passenger> passengerList;
    private static List<PassengerDTO> passengerDTOList;

    private List<Reservation> reservationList;

    private static final String passengerName = "Passenger name";
    private static final Long passengerId = 1L;
    private static final int NUM_OF_REPEATS = 5;

    @BeforeEach
    void setReservationList() {
        Ticket ticket = new Ticket();
        ticket.setId(1000L);
        ticket.setPrice(333L);
        ticket.setReserved(false);
        ticket.setFlight(flightList.get(0));

        reservationList = new ArrayList<>();
        for(int i = 0; i < NUM_OF_REPEATS; i++) {
            Reservation reservation = new Reservation();
            reservation.setId((long) i);
            reservation.setPassenger(passenger);
            reservation.setTicket(ticket);
            reservationList.add(reservation);
        }
    }

    @BeforeAll
    static void setUp() {
        passenger = new Passenger();
        passenger.setId(passengerId);
        passenger.setName(passengerName);
        passenger.setPassportNumber("Passport number");

        Airline airline = new Airline();
        airline.setId(100L);
        airline.setName("Airline name");

        flightList = new ArrayList<>();
        for(int i = 0; i < NUM_OF_REPEATS; i++) {
            Flight flight = new Flight();
            flight.setId((long) i);
            flight.setDepartureTown("Departure" + i);
            flight.setArrivalTown("Arrival" + i);
            flight.setAirline(airline);
            flightList.add(flight);
        }
        passenger.setFlights(flightList);

        passengerList = new ArrayList<>();
        for(int i = 0; i < NUM_OF_REPEATS; i++) {
            Passenger passenger = new Passenger();
            passenger.setId((long) i);
            passenger.setName("Name" + i);
            passenger.setPassportNumber("ABC" + i);
            passengerList.add(passenger);
        }

        passengerDTOList = notMockConvertModelToDTO.convertToDTOList(passengerList,
                notMockConvertModelToDTO::passengerConversion);
    }

    @Test
    void testFindAllPassengers_Valid() {
        when(passengerRepository.findAll()).thenReturn(passengerList);
        doReturn(passengerDTOList).when(convertModelToDTO).convertToDTOList(eq(passengerList), any());

        List<PassengerDTO> result = passengerService.findAllPassengers();

        assertEquals(NUM_OF_REPEATS, result.size());
        for(int i = 0; i < NUM_OF_REPEATS; i++) {
            assertEquals(i, result.get(i).getId());
            assertEquals("Name" + i, result.get(i).getName());
            assertEquals("ABC" + i, result.get(i).getPassportNumber());
        }
    }

    @Test
    void testFindAllPassengers_NoPassengersExist() {
        when(passengerRepository.findAll()).thenReturn(new ArrayList<>());

        List<PassengerDTO> result = passengerService.findAllPassengers();

        assertEquals(0, result.size());
    }

    @Test
    void testFindPassengerById_Valid() {
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(passenger));
        PassengerDTO passengerDTO = notMockConvertModelToDTO.passengerConversion(passenger);
        when(convertModelToDTO.passengerConversion(passenger)).thenReturn(passengerDTO);

        Optional<PassengerDTO> optionalResult = passengerService.findPassengerById(passengerId);
        assertTrue(optionalResult.isPresent());
        PassengerDTO result = optionalResult.get();
        assertEquals(passengerDTO.getId(), result.getId());
    }

    @Test
    void testFindPassengerById_NoPassengerExists() {
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> passengerService.findPassengerById(passengerId));
    }

    @Test
    void testFindPassengersByName_Valid() {
        when(passengerRepository.findByName(passengerName)).thenReturn(passengerList);
        doReturn(passengerDTOList).when(convertModelToDTO).convertToDTOList(eq(passengerList), any());

        List<PassengerDTO> result = passengerService.findPassengersByName(passengerName);

        assertEquals(NUM_OF_REPEATS, result.size());
        for(int i = 0; i < NUM_OF_REPEATS; i++) {
            assertEquals(i, result.get(i).getId());
            assertEquals("Name" + i, result.get(i).getName());
            assertEquals("ABC" + i, result.get(i).getPassportNumber());
        }
    }

    @Test
    void testFindPassengersByName_NoPassengersExist() {
        when(passengerRepository.findByName(passengerName)).thenReturn(new ArrayList<>());

        List<PassengerDTO> result = passengerService.findPassengersByName(passengerName);

        assertEquals(0, result.size());
    }

    @Test
    void testFindAllPassengerFlights_Valid() {
        List<FlightDTO> flightDTOList = notMockConvertModelToDTO.convertToDTOList(passenger.getFlights(),
                notMockConvertModelToDTO::flightConversion);
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(passenger));
        doReturn(flightDTOList).when(convertModelToDTO).convertToDTOList(eq(passenger.getFlights()), any());

        List<FlightDTO> result = passengerService.findAllPassengerFlights(passengerId);

        assertEquals(flightDTOList.size(), result.size());
        for (int i = 0; i < flightDTOList.size(); i++) {
            assertEquals(flightDTOList.get(i).getId(), result.get(i).getId());
            assertEquals(flightDTOList.get(i).getDepartureTown(), result.get(i).getDepartureTown());
            assertEquals(flightDTOList.get(i).getArrivalTown(), result.get(i).getArrivalTown());
        }
    }

    @Test
    void testFindAllPassengerFlights_NoPassengerExists() {
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> passengerService.findAllPassengerFlights(passengerId));
    }

    @Test
    void testFindAllPassengerReservations_Valid() {
        passenger.setReservations(reservationList);
        List<ReservationDTO> reservationDTOList = notMockConvertModelToDTO.convertToDTOList(reservationList,
                notMockConvertModelToDTO::reservationConversion);
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(passenger));
        doReturn(reservationDTOList).when(convertModelToDTO).convertToDTOList(eq(reservationList), any());

        List<ReservationDTO> result = passengerService.findAllPassengerReservations(passengerId);

        assertEquals(reservationDTOList.size(), result.size());
        for (int i = 0; i < reservationDTOList.size(); i++) {
            assertEquals(reservationDTOList.get(i).getId(), result.get(i).getId());
            assertEquals(reservationDTOList.get(i).getPassenger(), result.get(i).getPassenger());
            assertEquals(reservationDTOList.get(i).getTicket(), result.get(i).getTicket());
        }
    }

    @Test
    void testFindAllPassengerReservations_NoPassengerExists() {
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> passengerService.findAllPassengerFlights(passengerId));
    }

    @Test
    void testSaveOrUpdatePassenger_Valid() {
        PassengerDTO passengerDTO = notMockConvertModelToDTO.passengerConversion(passenger);
        when(passengerRepository.save(passenger)).thenReturn(passenger);
        when(convertModelToDTO.passengerConversion(passenger)).thenReturn(passengerDTO);

        PassengerDTO result = passengerService.saveOrUpdatePassenger(passenger);

        verify(passengerRepository, times(1)).save(passenger);
        assertEquals(passengerDTO, result);
    }

    @Test
    void testSaveOrUpdatePassenger_NotValidName() {
        passenger.setName(null);
        assertThrows(BadRequestException.class, () -> passengerService.saveOrUpdatePassenger(passenger));
        passenger.setName(passengerName);
    }

    @Test
    void testSaveOrUpdatePassenger_NotValidPassportNumber() {
        passenger.setPassportNumber(null);
        assertThrows(BadRequestException.class, () -> passengerService.saveOrUpdatePassenger(passenger));
        passenger.setPassportNumber("Passport number");
    }

    @Test
    void testDeletePassenger_Valid() {
        passenger.setReservations(reservationList);
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.of(passenger));

        passengerService.deletePassenger(passengerId);

        verify(passengerRepository, times(1)).delete(passenger);
        verify(reservationService, times(NUM_OF_REPEATS)).deleteReservation(anyLong());
    }

    @Test
    void testDeletePassenger_NoPassengerExists() {
        when(passengerRepository.findById(passengerId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> passengerService.deletePassenger(passengerId));
    }
}
