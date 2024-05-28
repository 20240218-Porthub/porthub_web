package hello.example.porthub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.filter.HiddenHttpMethodFilter;
@EnableScheduling
@SpringBootApplication
public class PorthubApplication {

	public static void main(String[] args) {
		SpringApplication.run(PorthubApplication.class, args);
	}
	@Bean
	public HiddenHttpMethodFilter hiddenHttpMethodFilter(){
		return new HiddenHttpMethodFilter();
	}

}
