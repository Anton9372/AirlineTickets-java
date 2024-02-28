package airline.tickets.service.impl;

import airline.tickets.model.Ticket;
import airline.tickets.service.TicketsService;
import airline.tickets.repository.TicketsRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Primary
public class InDBTicketsServiceImpl implements TicketsService {

    private final TicketsRepository repository;

    @Override
    public List<Ticket> findAllTickets() {
        return repository.findAll();
    }

    @Override
    public Ticket saveTicket(Ticket ticket) {
        return repository.save(ticket);
    }

    @Override
    public Ticket findByDepartureTown(String departureTown) {
        return repository.findByDepartureTown(departureTown);
    }

    @Override
    public Ticket updateTicket(Ticket ticket) {
        return repository.save(ticket);
    }

    @Override
    @Transactional
    public void deleteByDepartureTown(String departureTown) {
        repository.deleteByDepartureTown(departureTown);
    }
}
