package airline.tickets.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class TicketDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long price;
    private boolean isReserved;
    private FlightDTO flight;
}
