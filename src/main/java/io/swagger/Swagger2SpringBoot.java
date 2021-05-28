package io.swagger;

import io.swagger.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableOpenApi
//@EnableSwagger2
@ComponentScan(basePackages = { "io.swagger", "io.swagger.api" , "io.swagger.configuration"})
public class Swagger2SpringBoot {
    public static void main(String[] args) {
        SpringApplication.run(Swagger2SpringBoot.class, args);
    }
}
