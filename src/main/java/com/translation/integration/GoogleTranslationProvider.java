package com.translation.integration;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translate.TranslateOption;
import com.translation.exception.TranslationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class GoogleTranslationProvider implements TranslationProvider {
    
    private final Translate translate;
    
    public GoogleTranslationProvider() {
        try {
            this.translate = TranslateOptions.getDefaultInstance().getService();
            log.info("Google Cloud Translate provider initialized");
        } catch (Exception e) {
            log.error("Failed to initialize Google Cloud Translate: {}", e.getMessage());
            throw new TranslationException(
                "Failed to initialize Google Cloud Translate: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
    
    @Override
    public String translate(String text, String sourceLanguage, String targetLanguage) {
        try {
            if (!validateLanguagePair(sourceLanguage, targetLanguage)) {
                throw new TranslationException(
                    "Invalid language pair: " + sourceLanguage + " -> " + targetLanguage,
                    HttpStatus.BAD_REQUEST
                );
            }
            
            Translate.TranslateOption srcLang = TranslateOption.sourceLanguage(sourceLanguage);
            Translate.TranslateOption targetLang = TranslateOption.targetLanguage(targetLanguage);
            
            com.google.cloud.translate.Translation result = 
                translate.translate(text, srcLang, targetLang);
            
            log.debug("Translation completed: {} -> {}", sourceLanguage, targetLanguage);
            return result.getTranslatedText();
        } catch (Exception e) {
            log.error("Google translation error: {}", e.getMessage());
            throw new TranslationException(
                "Google translation failed: " + e.getMessage(),
                HttpStatus.SERVICE_UNAVAILABLE
            );
        }
    }
    
    @Override
    public List<String> batchTranslate(List<String> texts, String sourceLanguage, String targetLanguage) {
        return texts.stream()
            .map(text -> translate(text, sourceLanguage, targetLanguage))
            .collect(Collectors.toList());
    }
    
    @Override
    public Map<String, String> getSupportedLanguages() {
        try {
            Map<String, String> languages = new HashMap<>();
            
            // Google Translate supports 100+ languages
            // Common languages mapping
            languages.put("en", "English");
            languages.put("es", "Spanish");
            languages.put("fr", "French");
            languages.put("de", "German");
            languages.put("zh", "Chinese");
            languages.put("ja", "Japanese");
            languages.put("ko", "Korean");
            languages.put("ru", "Russian");
            languages.put("pt", "Portuguese");
            languages.put("hi", "Hindi");
            languages.put("ar", "Arabic");
            languages.put("it", "Italian");
            
            return languages;
        } catch (Exception e) {
            log.error("Failed to get supported languages: {}", e.getMessage());
            throw new TranslationException(
                "Failed to get supported languages: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
    
    @Override
    public boolean validateLanguagePair(String sourceLanguage, String targetLanguage) {
        return sourceLanguage != null && 
               targetLanguage != null && 
               sourceLanguage.length() == 2 && 
               targetLanguage.length() == 2 &&
               !sourceLanguage.equals(targetLanguage);
    }
    
    @Override
    public boolean healthCheck() {
        try {
            translate("hello", "en", "es");
            return true;
        } catch (Exception e) {
            log.error("Health check failed: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public String getProviderName() {
        return "GoogleTranslationProvider";
    }
}
