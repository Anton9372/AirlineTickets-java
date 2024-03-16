package airline.tickets.dto;

import lombok.Data;

@Data
public class TicketDTO {
    private Long id;
    private Long price;
    private boolean isReserved;
    private FlightDTO flight;
}
