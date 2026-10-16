package com.example.backend.repositories;


import com.example.backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // URL'deki :categorySlug ile kategoriyi bulur
    Optional<Category> findBySlug(String slug);

    // Üst menüde "Oyunlar", "Programlar" gibi gruplara göre filtrelemek istersen:
    // List<Category> findByMainGroup(String mainGroup);
}