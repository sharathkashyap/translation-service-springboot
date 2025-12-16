package com.translation.integration;

import com.translation.config.TranslationEngine;
import com.translation.config.TranslationProperties;
import com.translation.exception.TranslationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TranslationProviderFactory {
    
    private final GoogleTranslationProvider googleProvider;
    private final OpenAITranslationProvider openaiProvider;
    private final LocalTranslationProvider localProvider;
    private final TranslationProperties properties;
    
    public TranslationProvider getProvider() {
        try {
            TranslationEngine engine = TranslationEngine.fromValue(properties.getEngine());
            
            switch (engine) {
                case GOOGLE:
                    log.debug("Using Google Cloud Translate provider");
                    return googleProvider;
                case OPENAI:
                    log.debug("Using OpenAI provider");
                    return openaiProvider;
                case LOCAL:
                    log.debug("Using Local GPU provider");
                    return localProvider;
                default:
                    throw new TranslationException(
                        "Unknown translation engine: " + engine,
                        HttpStatus.INTERNAL_SERVER_ERROR
                    );
            }
        } catch (Exception e) {
            log.error("Failed to get translation provider: {}", e.getMessage());
            throw new TranslationException(
                "Failed to initialize translation provider: " + e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
    
    public TranslationProvider switchEngine(TranslationEngine engine) {
        log.info("Switching translation engine to: {}", engine);
        properties.setEngine(engine.getValue());
        return getProvider();
    }
}
