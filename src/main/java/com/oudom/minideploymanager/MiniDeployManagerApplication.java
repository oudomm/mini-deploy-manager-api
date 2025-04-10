package com.oudom.minideploymanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class MiniDeployManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniDeployManagerApplication.class, args);
    }

}
