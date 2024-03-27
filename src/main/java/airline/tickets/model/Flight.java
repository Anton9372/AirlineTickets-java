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
    private Long id;

    private String departureTown;
    private String arrivalTown;
    private LocalDateTime departureDateTime;

    @ManyToOne
    @JoinColumn(name = "airline_id")
    private Airline airline;

    @OneToMany(mappedBy = "flight")
    private List<Ticket> tickets = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "flight_history", joinColumns = @JoinColumn(name = "flight_id"),
            inverseJoinColumns = @JoinColumn(name = "passenger_id"))
    private List<Passenger> passengers = new ArrayList<>();
}