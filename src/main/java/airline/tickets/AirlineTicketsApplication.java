package airline.tickets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class AirlineTicketsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirlineTicketsApplication.class, args);
	}

}
