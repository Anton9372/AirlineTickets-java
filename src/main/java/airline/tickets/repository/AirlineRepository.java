package airline.tickets.repository;

import airline.tickets.model.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AirlineRepository extends JpaRepository<Airline, Long> {
    @Query("SELECT a FROM Airline a WHERE a.name = :name")
    List<Airline> findByName(@Param("name") String name);
}
