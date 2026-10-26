
package com.example.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class TranslationService {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    public TranslationService(
            @Value("${deepl.api-key:}") String apiKey,
            ObjectMapper objectMapper
    ) {
        this.apiKey = apiKey;
        this.objectMapper = objectMapper;

        String baseUrl = apiKey != null && apiKey.endsWith(":fx")
                ? "https://api-free.deepl.com"
                : "https://api.deepl.com";

        this.restClient = RestClient.create(baseUrl);
    }

    public String translate(String text, String targetLanguage) {

        if (text == null || text.isBlank()) {
            return text;
        }

        String target = targetLanguage.toUpperCase();

        if ("TR".equals(target)) {
            return text;
        }

        if (!List.of("EN", "DE").contains(target)) {
            throw new IllegalArgumentException(
                    "Desteklenmeyen dil: " + targetLanguage
            );
        }

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "DeepL API anahtarı tanımlanmamış."
            );
        }

        try {
            String response = restClient.post()
                    .uri("/v2/translate")
                    .header("Authorization", "DeepL-Auth-Key " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of(
                            "text", List.of(text),
                            "source_lang", "TR",
                            "target_lang", target
                    ))
                    .retrieve()
                    .body(String.class);

            JsonNode result = objectMapper.readTree(response)
                    .path("translations")
                    .path(0)
                    .path("text");

            if (!result.isTextual()) {
                throw new IllegalStateException(
                        "DeepL geçersiz yanıt döndürdü."
                );
            }

            return result.asText();

        } catch (Exception e) {
            throw new IllegalStateException(
                    "DeepL çeviri hatası: " + target, e
            );
        }
    }
}
