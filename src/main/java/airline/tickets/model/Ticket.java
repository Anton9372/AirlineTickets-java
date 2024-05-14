package airline.tickets.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long price;
    @NotNull
    private boolean isReserved;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;

    @Override
    public String toString() {
        return "id=" + id + ", " + "price=" + price + ", " + "isReserved=" + isReserved;
    }
}
