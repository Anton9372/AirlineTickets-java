package airline.tickets.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long price;

    private boolean isReserved;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;
}
