package airline.tickets.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "Airline Tickets API",
                description = "searching and booking airline tickets",
                version = "1.0",
                contact = @Contact(
                        name = "Anton",
                        email = "ap363402@gmail.com"
                )
        )
)

public class AirlineTicketsApiConfig {
}
