package airline.tickets.repository;

import airline.tickets.model.Ticket;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Repository
public class InMemTicketsDAO {
    private final List<Ticket> tickets = new ArrayList<>();

    public List<Ticket> findAllTickets() {
        return tickets;
    }

    public Ticket saveTicket(Ticket ticket) {
        tickets.add(ticket);
        return ticket;
    }

    public Ticket findByDepartureTown(String departureTown) {
        return tickets.stream()
                .filter(element -> element.getDepartureTown().equals(departureTown))
                .findFirst()
                .orElse(null);
    }

    public Ticket updateTicket(Ticket ticket) {
        var ticketIndex = IntStream.range(0, tickets.size())
                .filter(index -> tickets.get(index).getDepartureTown().equals(ticket.getDepartureTown()))
                .findFirst()
                .orElse(-1);
        if(ticketIndex != -1) {
            tickets.set(ticketIndex, ticket);
            return ticket;
        }
        return null;
    }

    public void deleteByDepartureTown(String departureTown) {
        var ticket = findByDepartureTown(departureTown);
        if(ticket != null) {
            tickets.remove(ticket);
        }
    }
}
