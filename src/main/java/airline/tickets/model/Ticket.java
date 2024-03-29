package airline.tickets.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private Long price;
    @NotNull
    private boolean isReserved;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private Flight flight;
}
