package com.sazzadur;

import com.sazzadur.repositories.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class SazzadurApplication {

    public static void main(String[] args) {
        SpringApplication.run(SazzadurApplication.class, args);
    }

}
