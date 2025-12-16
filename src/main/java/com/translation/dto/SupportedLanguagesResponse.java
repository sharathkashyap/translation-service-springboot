package com.translation.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupportedLanguagesResponse {
    private Map<String, String> languages;
    private String engine;
    private Integer total;
}

