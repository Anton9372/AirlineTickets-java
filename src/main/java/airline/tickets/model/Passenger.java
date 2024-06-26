package airline.tickets.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;
    @NotBlank
    private String passportNumber;

    @ManyToMany(mappedBy = "passengers", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Flight> flights = new ArrayList<>();

    @OneToMany(mappedBy = "passenger", fetch = FetchType.EAGER)
    private List<Reservation> reservations = new ArrayList<>();

    @Override
    public String toString() {
        return "id=" + id + ", " + "name=" + name + ", " + "passportNumber" + passportNumber;
    }
}
