package com.example.demo.cucumber.config;


import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

//classe che serve ad abilitare per cucumber il contesto spring
//senza questa configurazione, ogni bean sarebbe letto da cucumber come una semplice classe pojo
@CucumberContextConfiguration
@SpringBootTest
@Testcontainers
@ActiveProfiles("test")  //usiamo properties per i test
public class CucumberSpringConfiguration {

}


//La runner: fa partire Cucumber
//La config Spring: collega Cucumber a Spring Boot