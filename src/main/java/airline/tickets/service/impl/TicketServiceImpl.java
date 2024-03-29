package airline.tickets.service.impl;

import airline.tickets.aspect.AspectAnnotation;
import airline.tickets.cache.InMemoryCache;
import airline.tickets.dto.ReservationDTO;
import airline.tickets.dto.TicketDTO;
import airline.tickets.exception.BadRequestException;
import airline.tickets.exception.ResourceNotFoundException;
import airline.tickets.model.Flight;
import airline.tickets.model.Ticket;
import airline.tickets.repository.FlightRepository;
import airline.tickets.repository.TicketRepository;
import airline.tickets.service.ReservationService;
import airline.tickets.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final FlightRepository flightRepository;

    private final ReservationService reservationService;

    private final ConvertModelToDTOImpl convertModelToDTO;

    private final InMemoryCache<String, List<TicketDTO>> ticketCache;

    private static final String NO_TICKET_EXIST = "No Ticket found with id: ";
    private static final String NO_FLIGHT_EXIST = "No Flight found with id: ";

    @Override
    public List<TicketDTO> findAllTickets() {
        List<TicketDTO> cachedResult = ticketCache.get("allTickets");
        if (cachedResult != null) {
            return cachedResult;
        }
        List<Ticket> ticketList = ticketRepository.findAll();
        List<TicketDTO> result = convertModelToDTO.convertToDTOList(ticketList, convertModelToDTO::ticketConversion);
        ticketCache.put("allTickets", result);
        return result;
    }

    @Override
    @AspectAnnotation
    public List<TicketDTO> findByFlightId(final Long flightId) throws ResourceNotFoundException {
        String cacheKey = "flightId_" + flightId;
        List<TicketDTO> cachedResult = ticketCache.get(cacheKey);
        if (cachedResult != null) {
            return cachedResult;
        }
        Flight flight = flightRepository.findById(flightId).
                orElseThrow(() -> new ResourceNotFoundException(NO_FLIGHT_EXIST + flightId));
        List<Ticket> ticketList = flight.getTickets();
        List<TicketDTO> result = convertModelToDTO.convertToDTOList(ticketList, convertModelToDTO::ticketConversion);
        ticketCache.put(cacheKey, result);
        return result;
    }

    private List<TicketDTO> findTicketsByFlightList(final List<Flight> flightList) {
        List<Ticket> ticketList = new ArrayList<>();
        for (Flight flight : flightList) {
            ticketList.addAll(flight.getTickets());
        }
        return convertModelToDTO.convertToDTOList(ticketList, convertModelToDTO::ticketConversion);
    }

    @Override
    public List<TicketDTO> findByDepartureTown(final String departureTown) {
        String cacheKey = "departureTown_" + departureTown;
        List<TicketDTO> cachedResult = ticketCache.get(cacheKey);
        if (cachedResult != null) {
            return cachedResult;
        }
        List<TicketDTO> result = findTicketsByFlightList(flightRepository.findByDepartureTown(departureTown));
        ticketCache.put(cacheKey, result);
        return result;
    }

    @Override
    public List<TicketDTO> findByArrivalTown(final String arrivalTown) {
        String cacheKey = "arrivalTown_" + arrivalTown;
        List<TicketDTO> cachedResult = ticketCache.get(cacheKey);
        if (cachedResult != null) {
            return cachedResult;
        }
        List<TicketDTO> result = findTicketsByFlightList(flightRepository.findByArrivalTown(arrivalTown));
        ticketCache.put(cacheKey, result);
        return result;
    }

    @Override
    public List<TicketDTO> findByDepartureTownAndArrivalTown(final String departureTown, final String arrivalTown) {
        String cacheKey = "departureTown_" + departureTown + "_arrivalTown_" + arrivalTown;
        List<TicketDTO> cachedResult = ticketCache.get(cacheKey);
        if (cachedResult != null) {
            return cachedResult;
        }
        List<TicketDTO> result = findTicketsByFlightList(flightRepository.
                findByDepartureTownAndArrivalTown(departureTown, arrivalTown));
        ticketCache.put(cacheKey, result);
        return result;
    }

    @Override
    public List<TicketDTO> findUnreserved(final List<TicketDTO> ticketDTOList) {
        List<TicketDTO> unreservedTicketDTOList = new ArrayList<>();
        for (TicketDTO ticketDTO : ticketDTOList) {
            if (!ticketDTO.isReserved()) {
                unreservedTicketDTOList.add(ticketDTO);
            }
        }
        return unreservedTicketDTOList;
    }

    @Override
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
        ticketCache.clear();
        return convertModelToDTO.ticketConversion(ticket);
    }

    @Override
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
        for (int i = 0; i < numOfTickets; i++) {
            Ticket newTicket = new Ticket();
            newTicket.setPrice(ticket.getPrice());
            newTicket.setReserved(ticket.isReserved());
            newTicket.setFlight(ticket.getFlight());
            saveOrUpdateTicket(newTicket, flightId);
        }
        List<Ticket> ticketList = flight.getTickets();
        ticketCache.clear();
        return convertModelToDTO.convertToDTOList(ticketList, convertModelToDTO::ticketConversion);
    }

    @Override
    public void deleteTicket(final Long ticketId) throws ResourceNotFoundException {
        Ticket ticket = ticketRepository.findById(ticketId).
                orElseThrow(() -> new ResourceNotFoundException(NO_TICKET_EXIST + ticketId));
        if (ticket.isReserved()) {
            Optional<ReservationDTO> optionalReservationDTO = reservationService.findByTicketId(ticketId);
            optionalReservationDTO.ifPresent(reservationDTO -> reservationService.
                    deleteReservation(reservationDTO.getId()));
        }
        Flight flight = ticket.getFlight();
        List<Ticket> ticketList = flight.getTickets();
        ticketList.remove(ticket);
        flight.setTickets(ticketList);
        ticketCache.clear();
        ticketRepository.delete(ticket);
    }
}