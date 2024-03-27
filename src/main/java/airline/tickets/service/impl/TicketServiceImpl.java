package airline.tickets.service.impl;

import airline.tickets.cache.InMemoryCache;
import airline.tickets.dto.ReservationDTO;
import airline.tickets.dto.TicketDTO;
import airline.tickets.model.Flight;
import airline.tickets.model.Ticket;
import airline.tickets.repository.FlightRepository;
import airline.tickets.repository.TicketRepository;
import airline.tickets.service.ReservationService;
import airline.tickets.service.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
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
    public List<TicketDTO> findByFlightId(Long flightId) {
        String cacheKey = "flightId_" + flightId;
        List<TicketDTO> cachedResult = ticketCache.get(cacheKey);
        if (cachedResult != null) {
            return cachedResult;
        }
        List<Ticket> ticketList = ticketRepository.findByFlightId(flightId);
        List<TicketDTO> result = convertModelToDTO.convertToDTOList(ticketList, convertModelToDTO::ticketConversion);
        ticketCache.put(cacheKey, result);
        return result;
    }

    private List<TicketDTO> findTicketsByFlightList(List<Flight> flightList) {
        List<Ticket> ticketList = new ArrayList<>();
        for (Flight flight : flightList) {
            ticketList.addAll(flight.getTickets());
        }
        return convertModelToDTO.convertToDTOList(ticketList, convertModelToDTO::ticketConversion);
    }


    @Override
    public List<TicketDTO> findByDepartureTown(String departureTown) {
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
    public List<TicketDTO> findByArrivalTown(String arrivalTown) {
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
    public List<TicketDTO> findByDepartureTownAndArrivalTown(String departureTown, String arrivalTown) {
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
    public List<TicketDTO> findUnreserved(List<TicketDTO> ticketDTOList) {
        List<TicketDTO> unreservedTicketDTOList = new ArrayList<>();
        for (TicketDTO ticketDTO : ticketDTOList) {
            if (!ticketDTO.isReserved()) {
                unreservedTicketDTOList.add(ticketDTO);
            }
        }
        return unreservedTicketDTOList;
    }

    @Override
    public TicketDTO saveOrUpdateTicket(Ticket ticket, Long flightId) {
        Optional<Flight> optionalFlight = flightRepository.findById(flightId);
        if (optionalFlight.isPresent()) {
            Flight flight = optionalFlight.get();
            ticket.setFlight(flight);
            ticketRepository.save(ticket);
            ticketCache.clear();
            return convertModelToDTO.ticketConversion(ticket);
        } else {
            return null;
        }
    }

    @Override
    public List<TicketDTO> saveNumOfTickets(Ticket ticket, Long flightId, int numOfTickets) {
        Optional<Flight> optionalFlight = flightRepository.findById(flightId);
        if (optionalFlight.isPresent()) {
            for (int i = 0; i < numOfTickets; i++) {
                Ticket newTicket = new Ticket();
                newTicket.setPrice(ticket.getPrice());
                newTicket.setReserved(ticket.isReserved());
                newTicket.setFlight(ticket.getFlight());
                saveOrUpdateTicket(newTicket, flightId);
            }
            List<Ticket> ticketList = optionalFlight.get().getTickets();
            ticketCache.clear();
            return convertModelToDTO.convertToDTOList(ticketList, convertModelToDTO::ticketConversion);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void deleteTicket(Long ticketId) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(ticketId);
        if (optionalTicket.isPresent()) {
            Ticket ticket = optionalTicket.get();
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
}