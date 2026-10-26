package com.example.backend.dto;

public record CategoryResponse(
        Long id,
        String mainGroup,
        String name,
        String slug,
        String headerTitle,
        String description,
        String backLinkText,
        String aboutTitle
) {}