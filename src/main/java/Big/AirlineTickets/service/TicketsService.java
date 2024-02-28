package Big.AirlineTickets.service;

import Big.AirlineTickets.model.Ticket;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public interface TicketsService {
    List<Ticket> findAllTickets();
    Ticket saveTicket(Ticket ticket);
    Ticket findByDepartureTown(String departureTown);
    Ticket updateTicket(Ticket ticket);
    void deleteByDepartureTown(String departureTown);
}
