package airline.tickets.dto;

import lombok.Data;

@Data
public class ReservationDTO {
    private Long id;
    private PassengerDTO passenger;
    private TicketDTO ticket;
}
