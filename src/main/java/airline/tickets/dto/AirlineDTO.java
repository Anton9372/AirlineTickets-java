package airline.tickets.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class AirlineDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
}
