package airline.tickets.service;

import airline.tickets.aspect.AspectAnnotation;

import airline.tickets.dto.ReservationDTO;
import airline.tickets.dto.TicketDTO;
import airline.tickets.exception.BadRequestException;
import airline.tickets.exception.ResourceNotFoundException;
import airline.tickets.model.Flight;
import airline.tickets.model.Ticket;
import airline.tickets.repository.FlightRepository;
import airline.tickets.repository.TicketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final FlightRepository flightRepository;

    private final ReservationService reservationService;

    private final ConvertModelToDTO convertModelToDTO;

    private static final String NO_TICKET_EXIST = "No Ticket found with id: ";
    private static final String NO_FLIGHT_EXIST = "No Flight found with id: ";

    public List<TicketDTO> findAllTickets() {
        List<Ticket> ticketList = ticketRepository.findAll();
        return convertModelToDTO.convertToDTOList(ticketList, convertModelToDTO::ticketConversion);
    }

    private List<TicketDTO> findAllTicketsByFlightList(final List<Flight> flightList) {
        return flightList.stream()
                .flatMap(flight -> flight.getTickets().stream())
                .map(convertModelToDTO::ticketConversion)
                .collect(Collectors.toList());
    }

    private List<TicketDTO> findUnreservedTicketsByFlightList(final List<Flight> flightList) {
        /*List<Ticket> ticketList = new ArrayList<>();
        for (Flight flight : flightList) {
            for(Ticket ticket : flight.getTickets()) {
                if(!ticket.isReserved()) {
                    ticketList.add(ticket)
                }
            }
        }
        return convertModelToDTO.convertToDTOList(ticketList, convertModelToDTO::ticketConversion);*/
        return flightList.stream()
                .flatMap(flight -> flight.getTickets().stream())
                .filter(ticket -> !ticket.isReserved())
                .map(convertModelToDTO::ticketConversion)
                .collect(Collectors.toList());
    }

    @AspectAnnotation
    public List<TicketDTO> findAllTicketsByFlightId(final Long flightId) throws ResourceNotFoundException {
        Flight flight = flightRepository.findById(flightId).
                orElseThrow(() -> new ResourceNotFoundException(NO_FLIGHT_EXIST + flightId));
        return findAllTicketsByFlightList(Collections.singletonList(flight));
    }

    public List<TicketDTO> findAllTicketsByDepartureTown(final String departureTown) {
        return findAllTicketsByFlightList(flightRepository.findByDepartureTown(departureTown));
    }

    public List<TicketDTO> findAllTicketsByArrivalTown(final String arrivalTown) {
        return findAllTicketsByFlightList(flightRepository.findByArrivalTown(arrivalTown));
    }

    public List<TicketDTO> findAllTicketsByDepartureTownAndArrivalTown(final String departureTown,
                                                                    final String arrivalTown) {
        return findAllTicketsByFlightList(flightRepository.
                findByDepartureTownAndArrivalTown(departureTown, arrivalTown));
    }

    @AspectAnnotation
    public List<TicketDTO> findUnreservedTicketsByFlightId(final Long flightId) throws ResourceNotFoundException {
        Flight flight = flightRepository.findById(flightId).
                orElseThrow(() -> new ResourceNotFoundException(NO_FLIGHT_EXIST + flightId));
        return findUnreservedTicketsByFlightList(Collections.singletonList(flight));
    }

    public List<TicketDTO> findUnreservedTicketsByDepartureTown(final String departureTown) {
        return findUnreservedTicketsByFlightList(flightRepository.findByDepartureTown(departureTown));
    }

    public List<TicketDTO> findUnreservedTicketsByArrivalTown(final String arrivalTown) {
        return findUnreservedTicketsByFlightList(flightRepository.findByArrivalTown(arrivalTown));
    }

    public List<TicketDTO> findUnreservedTicketsByDepartureTownAndArrivalTown(final String departureTown,
                                                                       final String arrivalTown) {
        return findUnreservedTicketsByFlightList(flightRepository.
                findByDepartureTownAndArrivalTown(departureTown, arrivalTown));
    }

    @AspectAnnotation
    public TicketDTO saveOrUpdateTicket(final Ticket ticket, final Long flightId)
            throws ResourceNotFoundException, BadRequestException {
        Flight flight = flightRepository.findById(flightId).
                orElseThrow(() -> new ResourceNotFoundException(NO_FLIGHT_EXIST + flightId));
        if (ticket.getPrice() == null) {
            throw new BadRequestException("Price must be provided");
        }
        ticket.setFlight(flight);
        ticketRepository.save(ticket);
        return convertModelToDTO.ticketConversion(ticket);
    }

    public List<TicketDTO> saveNumOfTickets(final Ticket ticket, final Long flightId, final int numOfTickets)
            throws ResourceNotFoundException, BadRequestException {
        Flight flight = flightRepository.findById(flightId).
                orElseThrow(() -> new ResourceNotFoundException(NO_FLIGHT_EXIST + flightId));
        if (ticket.getPrice() == null) {
            throw new BadRequestException("Price must be provided");
        }
        if (numOfTickets < 1) {
            throw new BadRequestException("Num of tickets must be more than one");
        }
        List<Ticket> ticketList = new ArrayList<>();
        for (int i = 0; i < numOfTickets; i++) {
            Ticket newTicket = new Ticket();
            newTicket.setPrice(ticket.getPrice());
            newTicket.setReserved(ticket.isReserved());
            newTicket.setFlight(flight);
            saveOrUpdateTicket(newTicket, flightId);
            ticketList.add(newTicket);
        }
        return convertModelToDTO.convertToDTOList(ticketList, convertModelToDTO::ticketConversion);
    }

    public void deleteTicket(final Long ticketId) throws ResourceNotFoundException {
        Ticket ticket = ticketRepository.findById(ticketId).
                orElseThrow(() -> new ResourceNotFoundException(NO_TICKET_EXIST + ticketId));
        if (ticket.isReserved()) {
            Optional<ReservationDTO> optionalReservationDTO = reservationService.findReservationByTicketId(ticketId);
            optionalReservationDTO.ifPresent(reservationDTO -> reservationService.
                    deleteReservation(reservationDTO.getId()));
        }
        Flight flight = ticket.getFlight();
        List<Ticket> ticketList = flight.getTickets();
        ticketList.remove(ticket);
        flight.setTickets(ticketList);
        ticketRepository.delete(ticket);
    }
}