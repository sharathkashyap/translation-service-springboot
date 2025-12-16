package com.translation;

import com.translation.dto.TranslateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TranslationServiceApplicationTests {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void testHealthCheck() throws Exception {
        mockMvc.perform(get("/api/translate/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.healthy").exists());
    }
    
    @Test
    void testGetLanguages() throws Exception {
        mockMvc.perform(get("/api/translate/languages"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.languages").exists())
            .andExpect(jsonPath("$.total").isNumber());
    }
    
    @Test
    void testTranslateWithInvalidLanguage() throws Exception {
        TranslateRequest request = TranslateRequest.builder()
            .text("Hello")
            .sourceLanguage("en")
            .targetLanguage("en")  // Same language
            .build();
        
        mockMvc.perform(post("/api/translate/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    void testTranslateWithEmptyText() throws Exception {
        TranslateRequest request = TranslateRequest.builder()
            .text("")  // Empty text
            .sourceLanguage("en")
            .targetLanguage("es")
            .build();
        
        mockMvc.perform(post("/api/translate/")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }
    
    @Test
    void testServiceInfo() throws Exception {
        mockMvc.perform(get("/api/translate/info"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.service").exists())
            .andExpect(jsonPath("$.version").exists());
    }
}
