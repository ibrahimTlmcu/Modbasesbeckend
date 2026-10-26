package com.example.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(
        name = "mod_item_translations",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_mod_translation_language",
                        columnNames = {"mod_item_id", "language_code"}
                ),
                @UniqueConstraint(
                        name = "uk_mod_translation_slug_language",
                        columnNames = {"language_code", "slug"}
                )
        }
)
public class ModItemTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mod_item_id", nullable = false)
    @JsonIgnore
    private ModItem modItem;

    @Column(name = "language_code", nullable = false, length = 5)
    private String languageCode;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 200)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String shortDescription;

    @Column(length = 200)
    private String badgeText;

    @Column(columnDefinition = "TEXT")
    private String aboutDescription;

    @ElementCollection
    @CollectionTable(
            name = "mod_translation_gallery_images",
            joinColumns = @JoinColumn(name = "translation_id")
    )
    @AttributeOverrides({
            @AttributeOverride(
                    name = "imageUrl",
                    column = @Column(name = "image_url", nullable = false)
            ),
            @AttributeOverride(
                    name = "altText",
                    column = @Column(name = "alt_text")
            )
    })
    @OrderColumn(name = "position")
    private List<GalleryImage> gallery = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "mod_translation_features",
            joinColumns = @JoinColumn(name = "translation_id")
    )
    @Column(name = "feature", columnDefinition = "TEXT")
    @OrderColumn(name = "position")
    private List<String> features = new ArrayList<>();

    @Column(length = 2048)
    private String downloadUrl;

    @Column(length = 250)
    private String downloadBtnText;

    @Column(columnDefinition = "TEXT")
    private String installNote;

    @Column(length = 250)
    private String videoSectionTitle;

    @Column(columnDefinition = "TEXT")
    private String videoDescription;

    @Column(length = 1000)
    private String youtubeEmbedUrl;

    @Column(length = 250)
    private String iframeTitle;
}