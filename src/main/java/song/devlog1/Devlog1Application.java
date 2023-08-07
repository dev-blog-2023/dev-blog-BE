package song.devlog1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Devlog1Application {

	public static void main(String[] args) {
		SpringApplication.run(Devlog1Application.class, args);
	}

}
