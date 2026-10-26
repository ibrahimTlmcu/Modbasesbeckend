package com.example.backend.repositories;

import com.example.backend.entity.CategoryTranslation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryTranslationRepository
        extends JpaRepository<CategoryTranslation, Long> {

    List<CategoryTranslation> findByLanguageCodeOrderByNameAsc(
            String languageCode
    );

    Optional<CategoryTranslation> findByLanguageCodeAndSlug(
            String languageCode,
            String slug
    );

    Optional<CategoryTranslation> findByCategory_IdAndLanguageCode(
            Long categoryId,
            String languageCode
    );
}