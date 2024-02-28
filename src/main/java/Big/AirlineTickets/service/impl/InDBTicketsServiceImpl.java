package Big.AirlineTickets.service.impl;

import Big.AirlineTickets.model.Ticket;
import Big.AirlineTickets.repository.TicketsRepository;
import Big.AirlineTickets.service.TicketsService;
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
