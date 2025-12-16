package com.translation.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "translation")
public class TranslationProperties {
    
    private String engine = "google";
    
    private GoogleConfig google = new GoogleConfig();
    private OpenaiConfig openai = new OpenaiConfig();
    private LocalConfig local = new LocalConfig();
    private ApiConfig api = new ApiConfig();
    
    @Data
    public static class GoogleConfig {
        private String projectId;
        private String credentialsPath;
    }
    
    @Data
    public static class OpenaiConfig {
        private String apiKey;
        private String model = "gpt-3.5-turbo";
    }
    
    @Data
    public static class LocalConfig {
        private String modelName = "facebook/nllb-200-distilled-600M";
        private String device = "cuda";
        private String precision = "float32";
        private Integer batchSize = 8;
        private Integer maxLength = 512;
    }
    
    @Data
    public static class ApiConfig {
        private Integer maxTextLength = 5000;
        private Integer maxBatchSize = 100;
        private Boolean cacheEnabled = true;
        private Integer cacheTtl = 3600;
    }
}
