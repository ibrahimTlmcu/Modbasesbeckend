package com.example.backend.repositories;

import com.example.backend.entity.ModItemTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ModItemTranslationRepository
        extends JpaRepository<ModItemTranslation, Long> {

    List<ModItemTranslation> findByLanguageCodeOrderByTitleAsc(
            String languageCode
    );

    List<ModItemTranslation>
    findByLanguageCodeAndModItem_Category_IdOrderByTitleAsc(
            String languageCode,
            Long categoryId
    );

    Optional<ModItemTranslation> findByLanguageCodeAndSlug(
            String languageCode,
            String slug
    );

    Optional<ModItemTranslation>
    findByLanguageCodeAndSlugAndModItem_Category_Id(
            String languageCode,
            String slug,
            Long categoryId
    );
}