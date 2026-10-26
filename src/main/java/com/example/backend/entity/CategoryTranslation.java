package com.example.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(
        name = "category_translations",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_category_translation_language",
                        columnNames = {"category_id", "language_code"}
                ),
                @UniqueConstraint(
                        name = "uk_category_translation_slug_language",
                        columnNames = {"language_code", "slug"}
                )
        }
)
public class CategoryTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore
    private Category category;

    @Column(name = "language_code", nullable = false, length = 5)
    private String languageCode;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, length = 180)
    private String slug;

    @Column(length = 100)
    private String mainGroup;

    @Column(length = 250)
    private String headerTitle;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 200)
    private String backLinkText;

    @Column(length = 160)
    private String aboutTitle;
}