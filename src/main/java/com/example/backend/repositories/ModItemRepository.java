package com.example.backend.repositories;

import com.example.backend.entity.ModItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModItemRepository extends JpaRepository<ModItem, Long> {

    // Kategori sayfasına girildiğinde o kategorideki tüm modları/programları getirir
    // Örn: /api/categories/minecraft-modlar
    List<ModItem> findByCategory_Slug(String categorySlug);

    // Detay sayfasına girildiğinde ilgili kategorinin altındaki ilgili modu getirir
    // Örn: /api/mods/programlar/ccleaner
    Optional<ModItem> findByCategory_SlugAndSlug(String categorySlug, String modSlug);
}