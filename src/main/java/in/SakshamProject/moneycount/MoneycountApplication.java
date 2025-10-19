package in.SakshamProject.moneycount;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@SpringBootApplication
public class MoneycountApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoneycountApplication.class, args);
	}

}
