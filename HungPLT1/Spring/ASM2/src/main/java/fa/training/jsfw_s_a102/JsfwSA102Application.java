package fa.training.jsfw_s_a102;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JsfwSA102Application {

    public static void main(String[] args) {
        SpringApplication.run(JsfwSA102Application.class, args);
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        return new MultipartConfigElement("", 10 * 1024 * 1024L, 10 * 1024 * 1024L, 0);
    }
}
