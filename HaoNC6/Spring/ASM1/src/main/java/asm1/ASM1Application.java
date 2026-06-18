package asm1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "asm1.entity")
@EnableJpaRepositories(basePackages = "asm1.repository")
public class ASM1Application {
    public static void main(String[] args) {
        SpringApplication.run(ASM1Application.class, args);
    }
}
