package com.example.backend.dto;

import com.example.backend.entity.GalleryImage;

import java.util.List;

public record ModItemTranslationRequest(
        String languageCode,
        String title,
        String slug,
        String shortDescription,
        String badgeText,
        List<GalleryImage> gallery,
        String aboutDescription,
        List<String> features,
        String downloadUrl,
        String downloadBtnText,
        String installNote,
        String videoSectionTitle,
        String videoDescription,
        String youtubeEmbedUrl,
        String iframeTitle
) {}