package airline.tickets.repository;

import airline.tickets.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query("SELECT f FROM Flight f WHERE f.departureTown = :departureTown")
    List<Flight> findByDepartureTown(@Param("departureTown") String departureTown);

    @Query("SELECT f FROM Flight f WHERE f.arrivalTown = :arrivalTown")
    List<Flight> findByArrivalTown(@Param("arrivalTown") String arrivalTown);

    @Query("SELECT f FROM Flight f WHERE f.departureTown = :departureTown AND f.arrivalTown = :arrivalTown")
    List<Flight> findByDepartureTownAndArrivalTown(
            @Param("departureTown") String departureTown,
            @Param("arrivalTown") String arrivalTown);
}
