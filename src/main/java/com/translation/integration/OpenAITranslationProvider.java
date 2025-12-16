package com.translation.integration;

import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.translation.exception.TranslationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OpenAITranslationProvider implements TranslationProvider {
    
    private final OpenAiService openAiService;
    private final String model;
    
    private static final Map<String, String> SUPPORTED_LANGUAGES = new HashMap<>();
    
    static {
        SUPPORTED_LANGUAGES.put("en", "English");
        SUPPORTED_LANGUAGES.put("es", "Spanish");
        SUPPORTED_LANGUAGES.put("fr", "French");
        SUPPORTED_LANGUAGES.put("de", "German");
        SUPPORTED_LANGUAGES.put("zh", "Chinese");
        SUPPORTED_LANGUAGES.put("ja", "Japanese");
        SUPPORTED_LANGUAGES.put("ko", "Korean");
        SUPPORTED_LANGUAGES.put("ru", "Russian");
        SUPPORTED_LANGUAGES.put("pt", "Portuguese");
        SUPPORTED_LANGUAGES.put("hi", "Hindi");
        SUPPORTED_LANGUAGES.put("ar", "Arabic");
        SUPPORTED_LANGUAGES.put("it", "Italian");
    }
    
    public OpenAITranslationProvider(
            @Value("${translation.openai.api-key:}") String apiKey,
            @Value("${translation.openai.model:gpt-3.5-turbo}") String model) {
        
        if (apiKey == null || apiKey.isBlank()) {
            throw new TranslationException(
                "OpenAI API key not configured",
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
        
        this.openAiService = new OpenAiService(apiKey);
        this.model = model;
        log.info("OpenAI Translation provider initialized with model: {}", model);
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
            
            String sourceLangName = SUPPORTED_LANGUAGES.getOrDefault(sourceLanguage, sourceLanguage);
            String targetLangName = SUPPORTED_LANGUAGES.getOrDefault(targetLanguage, targetLanguage);
            
            String systemPrompt = String.format(
                "You are a professional translator. Translate text accurately from %s to %s. " +
                "Only provide the translation, no additional text.",
                sourceLangName, targetLangName
            );
            
            List<ChatMessage> messages = new ArrayList<>();
            messages.add(new ChatMessage("system", systemPrompt));
            messages.add(new ChatMessage("user", text));
            
            ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(model)
                .messages(messages)
                .temperature(0.3)
                .maxTokens(2048)
                .build();
            
            var response = openAiService.createChatCompletion(request);
            String translatedText = response.getChoices().get(0).getMessage().getContent().trim();
            
            log.debug("OpenAI translation completed: {} -> {}", sourceLanguage, targetLanguage);
            return translatedText;
        } catch (Exception e) {
            log.error("OpenAI translation error: {}", e.getMessage());
            throw new TranslationException(
                "OpenAI translation failed: " + e.getMessage(),
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
        return new HashMap<>(SUPPORTED_LANGUAGES);
    }
    
    @Override
    public boolean validateLanguagePair(String sourceLanguage, String targetLanguage) {
        return sourceLanguage != null &&
               targetLanguage != null &&
               SUPPORTED_LANGUAGES.containsKey(sourceLanguage) &&
               SUPPORTED_LANGUAGES.containsKey(targetLanguage) &&
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
        return "OpenAITranslationProvider";
    }
}
