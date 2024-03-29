package airline.tickets.service;

import airline.tickets.dto.TicketDTO;
import airline.tickets.exception.BadRequestException;
import airline.tickets.exception.ResourceNotFoundException;
import airline.tickets.model.Ticket;

import java.util.List;

public interface TicketService {

    List<TicketDTO> findAllTickets();

    List<TicketDTO> findByFlightId(Long flightId) throws ResourceNotFoundException;

    List<TicketDTO> findByDepartureTown(String departureTown);

    List<TicketDTO> findByArrivalTown(String arrivalTown);

    List<TicketDTO> findByDepartureTownAndArrivalTown(String departureTown, String arrivalTown);

    List<TicketDTO> findUnreserved(List<TicketDTO> ticketDTOList);

    TicketDTO saveOrUpdateTicket(Ticket ticket, Long flightId) throws ResourceNotFoundException, BadRequestException;

    List<TicketDTO> saveNumOfTickets(Ticket ticket, Long flightId, int numOfTickets) throws ResourceNotFoundException,
            BadRequestException;

    void deleteTicket(Long ticketId) throws ResourceNotFoundException;

}
