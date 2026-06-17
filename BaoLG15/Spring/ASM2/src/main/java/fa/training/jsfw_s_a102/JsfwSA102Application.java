package fa.training.jsfw_s_a102;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class JsfwSA102Application {

    public static void main(String[] args) {
        SpringApplication.run(JsfwSA102Application.class, args);
    }

}
