package com.example.backend.dto;

public record CategoryTranslationRequest(
        String languageCode,
        String name,
        String slug,
        String mainGroup,
        String headerTitle,
        String description,
        String backLinkText,
        String aboutTitle
) {}