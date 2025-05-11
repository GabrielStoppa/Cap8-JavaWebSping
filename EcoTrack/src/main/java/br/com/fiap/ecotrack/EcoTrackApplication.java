package br.com.fiap.ecotrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EcoTrackApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcoTrackApplication.class, args);
    }
}
