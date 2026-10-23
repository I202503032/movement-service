package pe.edu.cibertec.movementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MovementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovementServiceApplication.class, args);
    }

}
