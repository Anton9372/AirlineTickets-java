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
    @JoinColumn(name = "airlineId")
    private Airline airline;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "flight_passenger", joinColumns = @JoinColumn(name = "flightId"),
                inverseJoinColumns = @JoinColumn(name = "passengerId"))
    private List<Passenger> passengers = new ArrayList<>();
}