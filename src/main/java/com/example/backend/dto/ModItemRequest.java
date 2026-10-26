package com.example.backend.dto;

import java.util.List;

public record ModItemRequest(
        Long categoryId,
        String categorySlug,
        String thumbnailUrl,
        List<ModItemTranslationRequest> translations
) {}