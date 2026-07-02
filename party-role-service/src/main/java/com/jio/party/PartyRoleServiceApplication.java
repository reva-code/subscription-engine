package com.jio.party;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PartyRoleServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PartyRoleServiceApplication.class, args);
    }
}
