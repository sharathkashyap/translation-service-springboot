package com.translation.service;

import com.translation.dto.*;
import com.translation.integration.TranslationProvider;
import com.translation.integration.TranslationProviderFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranslationService {
    
    private final TranslationProviderFactory providerFactory;
    
    public TranslateResponse translate(TranslateRequest request) {
        try {
            long startTime = System.currentTimeMillis();
            TranslationProvider provider = providerFactory.getProvider();
            
            String translatedText = provider.translate(
                request.getText(),
                request.getSourceLanguage(),
                request.getTargetLanguage()
            );
            
            long duration = System.currentTimeMillis() - startTime;
            log.info("Translation completed in {}ms - {} -> {}",
                duration, request.getSourceLanguage(), request.getTargetLanguage());
            
            return TranslateResponse.builder()
                .originalText(request.getText())
                .translatedText(translatedText)
                .sourceLanguage(request.getSourceLanguage())
                .targetLanguage(request.getTargetLanguage())
                .engine(provider.getProviderName())
                .timestamp(LocalDateTime.now())
                .build();
        } catch (Exception e) {
            log.error("Translation error: {}", e.getMessage());
            throw e;
        }
    }
    
    public BatchTranslateResponse batchTranslate(BatchTranslateRequest request) {
        try {
            long startTime = System.currentTimeMillis();
            TranslationProvider provider = providerFactory.getProvider();
            
            List<String> translatedTexts = provider.batchTranslate(
                request.getTexts(),
                request.getSourceLanguage(),
                request.getTargetLanguage()
            );
            
            long duration = System.currentTimeMillis() - startTime;
            log.info("Batch translation completed in {}ms - {} texts translated",
                duration, request.getTexts().size());
            
            return BatchTranslateResponse.builder()
                .originalTexts(request.getTexts())
                .translatedTexts(translatedTexts)
                .sourceLanguage(request.getSourceLanguage())
                .targetLanguage(request.getTargetLanguage())
                .engine(provider.getProviderName())
                .count(request.getTexts().size())
                .timestamp(LocalDateTime.now())
                .build();
        } catch (Exception e) {
            log.error("Batch translation error: {}", e.getMessage());
            throw e;
        }
    }
    
    public SupportedLanguagesResponse getSupportedLanguages() {
        try {
            TranslationProvider provider = providerFactory.getProvider();
            var languages = provider.getSupportedLanguages();
            
            return SupportedLanguagesResponse.builder()
                .languages(languages)
                .engine(provider.getProviderName())
                .total(languages.size())
                .build();
        } catch (Exception e) {
            log.error("Error getting supported languages: {}", e.getMessage());
            throw e;
        }
    }
    
    public HealthCheckResponse healthCheck() {
        try {
            long startTime = System.currentTimeMillis();
            TranslationProvider provider = providerFactory.getProvider();
            boolean healthy = provider.healthCheck();
            double responseTime = System.currentTimeMillis() - startTime;
            
            return HealthCheckResponse.builder()
                .healthy(healthy)
                .engine(provider.getProviderName())
                .timestamp(LocalDateTime.now())
                .responseTimeMs(responseTime)
                .build();
        } catch (Exception e) {
            log.error("Health check error: {}", e.getMessage());
            return HealthCheckResponse.builder()
                .healthy(false)
                .engine("unknown")
                .timestamp(LocalDateTime.now())
                .responseTimeMs(0.0)
                .build();
        }
    }
}
