package com.example.backend.dto;

import java.util.List;

public record CategoryRequest(
        List<CategoryTranslationRequest> translations
) {}