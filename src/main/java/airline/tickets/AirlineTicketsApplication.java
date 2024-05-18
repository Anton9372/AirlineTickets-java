package airline.tickets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class AirlineTicketsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirlineTicketsApplication.class, args);
	}

}
