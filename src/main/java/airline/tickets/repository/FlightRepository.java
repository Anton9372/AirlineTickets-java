package airline.tickets.repository;

import airline.tickets.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    List<Flight> findByDepartureTown(String departureTown);

    List<Flight> findByArrivalTown(String arrivalTown);

    List<Flight> findByDepartureTownAndArrivalTown(String departureTown, String arrivalTown);

}
