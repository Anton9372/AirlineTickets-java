package airline.tickets.service.impl;

import airline.tickets.model.Ticket;
import airline.tickets.repository.InMemTicketsDAO;
import airline.tickets.service.TicketsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InMemTicketsServiceImpl implements TicketsService {

    private final InMemTicketsDAO repository;
    @Override
    public List<Ticket> findAllTickets() {
        return repository.findAllTickets();
    }

    @Override
    public Ticket saveTicket(Ticket ticket) {
        return repository.saveTicket(ticket);
    }
    @Override
    public Ticket findByDepartureTown(String departureTown) {
        return repository.findByDepartureTown(departureTown);
    }

    @Override
    public Ticket updateTicket(Ticket ticket) {
        return repository.updateTicket(ticket);
    }
    @Override
    public void deleteByDepartureTown(String departureTown) {
        repository.deleteByDepartureTown(departureTown);
    }

}
