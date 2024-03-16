package airline.tickets.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FlightDTO {
    private Long id;
    private String departureTown;
    private String arrivalTown;
    private LocalDateTime departureDateTime;
    private String airlineName;
}
