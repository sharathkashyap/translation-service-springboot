package com.translation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchTranslateRequest {
    @NotEmpty(message = "Texts list is required")
    @Size(min = 1, max = 100, message = "Must provide between 1 and 100 texts")
    private List<@NotBlank(message = "Text cannot be blank") String> texts;
    
    @NotBlank(message = "Source language is required")
    private String sourceLanguage;
    
    @NotBlank(message = "Target language is required")
    private String targetLanguage;
}
