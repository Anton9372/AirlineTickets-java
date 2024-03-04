package airline.tickets.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Airline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long airlineId;

    private String name;

    @OneToMany(mappedBy = "airline", cascade = {CascadeType.ALL})
    @JsonIgnore
    private List<Flight> flights = new ArrayList<>();
}
