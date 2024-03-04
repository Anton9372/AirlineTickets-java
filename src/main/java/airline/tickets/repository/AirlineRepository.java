package airline.tickets.repository;

import airline.tickets.model.Airline;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AirlineRepository extends JpaRepository<Airline, Long> {
    Airline findByName(String name);
    void deleteByName(String name);
}
