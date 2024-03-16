package airline.tickets.service.impl;

import airline.tickets.dto.TicketDTO;
import airline.tickets.model.Flight;
import airline.tickets.model.Ticket;
import airline.tickets.repository.FlightRepository;
import airline.tickets.repository.TicketRepository;
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
    private final ConvertModelToDTOImpl convertModelToDTO;

    @Override
    public List<TicketDTO> findAllTickets() {
        List<Ticket> ticketList = ticketRepository.findAll();
        return convertModelToDTO.convertToDTOList(ticketList, convertModelToDTO::ticketConversion);
    }

    @Override
    public List<TicketDTO> findByFlightId(Long flightId) {
        List<Ticket> ticketList = ticketRepository.findByFlightId(flightId);
        return convertModelToDTO.convertToDTOList(ticketList, convertModelToDTO::ticketConversion);
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
        return findTicketsByFlightList(flightRepository.findByDepartureTown(departureTown));
    }

    @Override
    public List<TicketDTO> findByArrivalTown(String arrivalTown) {
        return findTicketsByFlightList(flightRepository.findByArrivalTown(arrivalTown));
    }

    @Override
    public List<TicketDTO> findByDepartureTownAndArrivalTown(String departureTown, String arrivalTown) {
        return findTicketsByFlightList(flightRepository.findByDepartureTownAndArrivalTown(departureTown, arrivalTown));
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
            return convertModelToDTO.convertToDTOList(ticketList, convertModelToDTO::ticketConversion);
        } else {
            return Collections.emptyList();
        }
    }
}
