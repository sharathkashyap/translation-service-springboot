package com.translation.controller;

import com.translation.dto.*;
import com.translation.service.TranslationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/translate")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TranslationController {
    
    private final TranslationService translationService;
    
    /**
     * Translate single text
     */
    @PostMapping("/")
    public ResponseEntity<TranslateResponse> translate(@Valid @RequestBody TranslateRequest request) {
        log.info("Translation request: {} -> {}",
            request.getSourceLanguage(), request.getTargetLanguage());
        try {
            TranslateResponse response = translationService.translate(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Translation failed: {}", e.getMessage());
            throw e;
        }
    }
    
    /**
     * Batch translate multiple texts
     */
    @PostMapping("/batch")
    public ResponseEntity<BatchTranslateResponse> batchTranslate(@Valid @RequestBody BatchTranslateRequest request) {
        log.info("Batch translation request: {} texts, {} -> {}",
            request.getTexts().size(), request.getSourceLanguage(), request.getTargetLanguage());
        try {
            BatchTranslateResponse response = translationService.batchTranslate(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Batch translation failed: {}", e.getMessage());
            throw e;
        }
    }
    
    /**
     * Get supported languages
     */
    @GetMapping("/languages")
    public ResponseEntity<SupportedLanguagesResponse> getLanguages() {
        log.info("Fetching supported languages");
        try {
            SupportedLanguagesResponse response = translationService.getSupportedLanguages();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Failed to get languages: {}", e.getMessage());
            throw e;
        }
    }
    
    /**
     * Health check
     */
    @GetMapping("/health")
    public ResponseEntity<HealthCheckResponse> healthCheck() {
        log.info("Health check requested");
        try {
            HealthCheckResponse response = translationService.healthCheck();
            if (response.getHealthy()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
            }
        } catch (Exception e) {
            log.error("Health check error: {}", e.getMessage());
            throw e;
        }
    }
    
    /**
     * Service info
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> getInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("service", "Translation Service API");
        info.put("version", "1.0.0");
        info.put("status", "operational");
        return ResponseEntity.ok(info);
    }
}
