package airline.tickets.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flightId;

    private String departureTown;
    private String arrivalTown;
    private LocalDateTime departureDateTime;

    @ManyToOne
    @JoinColumn(name = "airline_id")
    private Airline airline;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "flight_passenger", joinColumns = @JoinColumn(name = "flight_id"),
                inverseJoinColumns = @JoinColumn(name = "passenger_id"))
    private List<Passenger> passengers = new ArrayList<>();
}