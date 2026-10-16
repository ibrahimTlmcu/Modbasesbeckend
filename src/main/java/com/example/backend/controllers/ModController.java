package com.example.backend.controllers;

import com.example.backend.entity.Category;
import com.example.backend.entity.ModItem;
import com.example.backend.repositories.CategoryRepository;
import com.example.backend.repositories.ModItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ModController {

    private final CategoryRepository categoryRepository;
    private final ModItemRepository modItemRepository;

    // Yardımcı Metot: Gelen değer sayıysa (örn: "4") ID ile, metinse (örn: "ets2") Slug ile kategoriyi bulur
    private Optional<Category> findCategoryByIdOrSlug(String idOrSlug) {
        if (idOrSlug != null && idOrSlug.matches("\\d+")) {
            return categoryRepository.findById(Long.parseLong(idOrSlug))
                    .or(() -> categoryRepository.findBySlug(idOrSlug));
        }
        return categoryRepository.findBySlug(idOrSlug);
    }

    // ==================== GET (OKUMA) İŞLEMLERİ ====================

    // 1. Tüm kategorileri getirir: GET -> http://localhost:8080/api/categories
    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // 2. Seçilen kategoriyi getirir (Hem ID hem Slug destekler): GET -> /api/categories/4 veya /api/categories/ets2
    @GetMapping("/categories/{categorySlug}")
    public ResponseEntity<Category> getCategoryBySlug(@PathVariable String categorySlug) {
        return findCategoryByIdOrSlug(categorySlug)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Kategorideki modları getirir (Hem ID hem Slug destekler): GET -> /api/categories/4/mods veya /api/categories/ets2/mods
    @GetMapping("/categories/{categorySlug}/mods")
    public List<ModItem> getModsByCategory(@PathVariable String categorySlug) {
        return findCategoryByIdOrSlug(categorySlug)
                .map(category -> modItemRepository.findByCategory_Slug(category.getSlug()))
                .orElse(Collections.emptyList());
    }

    // 4. Tüm modları getirir (Admin paneli listesi için): GET -> http://localhost:8080/api/mods
    @GetMapping("/mods")
    public List<ModItem> getAllMods() {
        return modItemRepository.findAll();
    }

    // 5. Mod detayını getirir (Hem ID hem Slug destekler): GET -> /api/mods/ets2/snowy-moon
    @GetMapping("/mods/{categorySlug}/{modSlug}")
    public ResponseEntity<ModItem> getModDetail(
            @PathVariable String categorySlug,
            @PathVariable String modSlug) {
        return findCategoryByIdOrSlug(categorySlug)
                .flatMap(category -> modItemRepository.findByCategory_SlugAndSlug(category.getSlug(), modSlug))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ==================== POST (EKLEME) İŞLEMLERİ ====================

    // 6. Yeni Kategori Ekleme: POST -> http://localhost:8080/api/categories
    @PostMapping("/categories")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category saved = categoryRepository.save(category);
        return ResponseEntity.ok(saved);
    }

    // 7. Kategoriye Yeni Mod Ekleme (Hem ID hem Slug destekler): POST -> /api/categories/4/mods veya /api/categories/ets2/mods
    @PostMapping("/categories/{categorySlug}/mods")
    public ResponseEntity<ModItem> createModByCategorySlug(
            @PathVariable String categorySlug,
            @RequestBody ModItem modItem) {
        return findCategoryByIdOrSlug(categorySlug)
                .map(category -> {
                    modItem.setCategory(category);
                    return ResponseEntity.ok(modItemRepository.save(modItem));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 8. Doğrudan Mod Ekleme (Alternatif): POST -> http://localhost:8080/api/mods
    @PostMapping("/mods")
    public ResponseEntity<ModItem> createModDirect(@RequestBody ModItem modItem) {
        if (modItem.getCategory() != null && modItem.getCategory().getId() != null) {
            categoryRepository.findById(modItem.getCategory().getId())
                    .ifPresent(modItem::setCategory);
        } else if (modItem.getCategory() != null && modItem.getCategory().getSlug() != null) {
            findCategoryByIdOrSlug(modItem.getCategory().getSlug())
                    .ifPresent(modItem::setCategory);
        }
        return ResponseEntity.ok(modItemRepository.save(modItem));
    }

    // ==================== DELETE (SİLME) İŞLEMLERİ ====================

    // 9. Kategori Silme (Hem ID hem Slug destekler): DELETE -> /api/categories/4 veya /api/categories/ets2
    @DeleteMapping("/categories/{idOrSlug}")
    public ResponseEntity<Void> deleteCategory(@PathVariable String idOrSlug) {
        Optional<Category> categoryOpt = findCategoryByIdOrSlug(idOrSlug);
        if (categoryOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        categoryRepository.delete(categoryOpt.get());
        return ResponseEntity.noContent().build();
    }

    // 10. Mod Silme: DELETE -> http://localhost:8080/api/mods/{id}
    @DeleteMapping("/mods/{id}")
    public ResponseEntity<Void> deleteMod(@PathVariable Long id) {
        if (!modItemRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        modItemRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}