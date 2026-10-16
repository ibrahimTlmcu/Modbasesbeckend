package com.example.backend.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GalleryImage {
    private String imageUrl;
    private String altText; // Örn: "CCleaner Sağlık Kontrolü ve Özet Ekranı"
}