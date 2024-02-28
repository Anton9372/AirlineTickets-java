package Big.AirlineTickets.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Ticket {
    @Id
    @GeneratedValue
    private Long id;
    private String departureTown;
    private String arrivalTown;
    private LocalDateTime dateTime;
    private double price;
}
