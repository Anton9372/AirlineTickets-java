package airline.tickets.service;

import airline.tickets.model.Ticket;

import java.util.List;

public interface TicketsService {
    List<Ticket> findAllTickets();
    Ticket saveTicket(Ticket ticket);
    Ticket findByDepartureTown(String departureTown);
    Ticket updateTicket(Ticket ticket);
    void deleteByDepartureTown(String departureTown);
}
