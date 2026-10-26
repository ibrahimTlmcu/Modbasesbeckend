package com.example.backend.entity;

import com.example.backend.entity.Category;
import com.example.backend.entity.GalleryImage;
import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.ArrayList;

import java.util.List;

@Entity
@Data
@Table(name = "mod_items")
public class ModItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Galeri Kartı Bilgileri ---
    private String title;             // Örn: "CCleaner Sistem Temizleme ve Optimizasyon"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Category category;

    @OneToMany(
            mappedBy = "modItem",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ModItemTranslation> translations = new ArrayList<>();
    @Column(unique = true)
    private String slug;              // Örn: "ccleaner" veya "optifine"
    private String thumbnailUrl;      // Kategori sayfasındaki kart görseli
    private String shortDescription;  // Kategori sayfasındaki kart açıklaması

    // --- Detay Sayfası Üst Kısım ---
    private String badgeText;         // Örn: "Sürüm: v6.28 Pro"

    // --- 4 Görselli Galeri ---
    @ElementCollection
    @CollectionTable(name = "mod_gallery_images", joinColumns = @JoinColumn(name = "mod_id"))
    private List<GalleryImage> gallery; // 4 adet görsel URL ve Alt metni

    // --- Sol Taraf: Bilgiler ---
    @Column(columnDefinition = "TEXT")
    private String aboutDescription;  // <p> içindeki uzun açıklama

    @ElementCollection
    @CollectionTable(name = "mod_features", joinColumns = @JoinColumn(name = "mod_id"))
    @Column(name = "feature")
    private List<String> features;    // <ul> <li> içindeki maddeler

    // --- Sağ Taraf: İndirme Kutusu ---
    private String downloadUrl;       // Örn: "https://www.ccleaner.com/tr-tr"
    private String downloadBtnText;   // Örn: "⬇️ CCleaner İndir (.exe)"

    @Column(columnDefinition = "TEXT")
    private String installNote;       // "İndirdiğiniz kurulum dosyasını çalıştırın..."

    // --- Alt Kısım: YouTube Video Bölümü ---
    private String videoSectionTitle; // Örn: "CCleaner Kullanım ve İpuçları Videosu"

    @Column(columnDefinition = "TEXT")
    private String videoDescription;  // <p class="video-desc"> içindeki metin
    private String youtubeEmbedUrl;   // Örn: "https://www.youtube.com/embed/8ZYt2MgTSSQ?si=HvJHM8Byn8wMiTSf"
    private String iframeTitle;       // Örn: "CCleaner Tutorial"


}