package airline.tickets.dto;

import lombok.Data;

@Data
public class PassengerDTO {
    private Long id;
    private String name;
    private String passportNumber;
}
