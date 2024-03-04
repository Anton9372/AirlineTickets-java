package airline.tickets.repository;

import airline.tickets.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
    Passenger findByName(String name);
    void deleteByName(String name);
}
