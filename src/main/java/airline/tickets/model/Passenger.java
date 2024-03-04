package airline.tickets.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long passengerId;

    private String name;
    private String passportNumber;

    @ManyToMany(mappedBy = "passengers", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JsonIgnore
    private List<Flight> flights = new ArrayList<>();
}
