package airline.tickets.repository;

import airline.tickets.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    Flight findByDepartureTown(String departureTown);
    void deleteByDepartureTown(String departureTown);
}
