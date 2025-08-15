package org.example.kuit_kac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class KuitKacApplication {

    public static void main(String[] args) {
        SpringApplication.run(KuitKacApplication.class, args);
    }

}
