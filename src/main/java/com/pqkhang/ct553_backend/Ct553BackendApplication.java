package com.pqkhang.ct553_backend;

import com.pqkhang.ct553_backend.infrastructure.config.context.DotEnvApplicationContextInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class Ct553BackendApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Ct553BackendApplication.class);
        springApplication.addInitializers(new DotEnvApplicationContextInitializer());
        springApplication.run(args);
    }

}
