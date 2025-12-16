package com.translation.integration;

import java.util.List;
import java.util.Map;

public interface TranslationProvider {
    
    /**
     * Translate text from source to target language
     */
    String translate(String text, String sourceLanguage, String targetLanguage);
    
    /**
     * Batch translate multiple texts
     */
    List<String> batchTranslate(List<String> texts, String sourceLanguage, String targetLanguage);
    
    /**
     * Get supported languages
     */
    Map<String, String> getSupportedLanguages();
    
    /**
     * Validate language pair
     */
    boolean validateLanguagePair(String sourceLanguage, String targetLanguage);
    
    /**
     * Health check
     */
    boolean healthCheck();
    
    /**
     * Get provider name
     */
    String getProviderName();
}
