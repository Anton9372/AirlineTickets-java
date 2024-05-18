package airline.tickets.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ReservationDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private PassengerDTO passenger;
    private TicketDTO ticket;
}
