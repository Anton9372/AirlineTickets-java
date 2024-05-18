package airline.tickets.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class FlightDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String departureTown;
    private String arrivalTown;
    private LocalDateTime departureDateTime;
    private AirlineDTO airline;
}
