package Big.AirlineTickets.service.impl;

import Big.AirlineTickets.model.Ticket;
import Big.AirlineTickets.service.TicketsService;
import Big.AirlineTickets.repository.InMemTicketsDAO;
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
