package com.translation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchTranslateResponse {
    private List<String> originalTexts;
    private List<String> translatedTexts;
    private String sourceLanguage;
    private String targetLanguage;
    private String engine;
    private Integer count;
    private LocalDateTime timestamp;
}
