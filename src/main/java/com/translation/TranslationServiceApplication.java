package com.translation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@ComponentScan(basePackages = "com.translation")
public class TranslationServiceApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(TranslationServiceApplication.class, args);
        log.info("╔════════════════════════════════════════════════════════════════╗");
        log.info("║  🌍 Translation Service API Started Successfully 🌍           ║");
        log.info("║  API Documentation: http://localhost:8080/swagger-ui.html    ║");
        log.info("║  Health Check: http://localhost:8080/api/translate/health    ║");
        log.info("╚════════════════════════════════════════════════════════════════╝");
    }
}
