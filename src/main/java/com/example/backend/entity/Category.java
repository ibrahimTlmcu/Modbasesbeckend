package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.ArrayList;

@Entity
@Data
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<ModItem> items;

    @OneToMany(
            mappedBy = "category",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<CategoryTranslation> translations = new ArrayList<>();

    private String mainGroup;   // Örn: "Oyunlar" veya "Programlar" (Üst menü gruplaması için)
    private String name;        // Örn: "Minecraft", "Mount & Blade II: Bannerlord", "Sistem Araçları"

    @Column(unique = true)
    private String slug;        // URL yapısı -> Örn: "minecraft-modlar", "programlar"

    private String headerTitle; // Galeri Başlığı -> Örn: "Minecraft Mod ve Eklenti Galerisi"
    private String description; // Galeri Alt Açıklaması

    // Detay sayfasındaki sabit metinleri kategoriye göre dinamikleştirmek için:
    private String backLinkText; // Örn: "← Program Galerisine Dön" veya "← Minecraft Modlarına Dön"
    private String aboutTitle;   // Örn: "Program Hakkında" veya "Mod Hakkında"

}