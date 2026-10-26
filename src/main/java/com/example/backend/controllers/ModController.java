package com.example.backend.controllers;

import com.example.backend.dto.CategoryRequest;
import com.example.backend.dto.CategoryResponse;
import com.example.backend.dto.ModItemRequest;
import com.example.backend.dto.ModItemResponse;
import com.example.backend.services.ModService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ModController {

    private final ModService modService;

    // GET /api/categories?lang=en
    @GetMapping("/categories")
    public List<CategoryResponse> getAllCategories(
            @RequestParam(defaultValue = "tr") String lang
    ) {
        return modService.getAllCategories(lang);
    }

    // GET /api/categories/minecraft-mods?lang=en
    // GET /api/categories/4?lang=en
    @GetMapping("/categories/{categorySlug}")
    public ResponseEntity<CategoryResponse> getCategory(
            @PathVariable String categorySlug,
            @RequestParam(defaultValue = "tr") String lang
    ) {
        return modService.getCategory(categorySlug, lang)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET /api/categories/minecraft-mods/mods?lang=en
    @GetMapping("/categories/{categorySlug}/mods")
    public List<ModItemResponse> getModsByCategory(
            @PathVariable String categorySlug,
            @RequestParam(defaultValue = "tr") String lang
    ) {
        return modService.getModsByCategory(categorySlug, lang);
    }

    // GET /api/mods?lang=en
    @GetMapping("/mods")
    public List<ModItemResponse> getAllMods(
            @RequestParam(defaultValue = "tr") String lang
    ) {
        return modService.getAllMods(lang);
    }

    // GET /api/mods/minecraft-mods/example-mod?lang=en
    @GetMapping("/mods/{categorySlug}/{modSlug}")
    public ResponseEntity<ModItemResponse> getModDetail(
            @PathVariable String categorySlug,
            @PathVariable String modSlug,
            @RequestParam(defaultValue = "tr") String lang
    ) {
        return modService.getModDetail(categorySlug, modSlug, lang)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /api/categories?lang=en
    @PostMapping("/categories")
    public ResponseEntity<CategoryResponse> createCategory(
            @RequestBody CategoryRequest request,
            @RequestParam(defaultValue = "tr") String lang
    ) {
        return ResponseEntity.ok(
                modService.createCategory(request, lang)
        );
    }

    // POST /api/categories/minecraft-mods/mods?lang=en
    @PostMapping("/categories/{categorySlug}/mods")
    public ResponseEntity<ModItemResponse> createModByCategory(
            @PathVariable String categorySlug,
            @RequestBody ModItemRequest request,
            @RequestParam(defaultValue = "tr") String lang
    ) {
        return ResponseEntity.ok(
                modService.createModByCategory(
                        categorySlug,
                        request,
                        lang
                )
        );
    }

    // POST /api/mods?lang=en
    @PostMapping("/mods")
    public ResponseEntity<ModItemResponse> createModDirect(
            @RequestBody ModItemRequest request,
            @RequestParam(defaultValue = "tr") String lang
    ) {
        return ResponseEntity.ok(
                modService.createModDirect(request, lang)
        );
    }

    // DELETE /api/categories/4?lang=tr
    @DeleteMapping("/categories/{idOrSlug}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable String idOrSlug,
            @RequestParam(defaultValue = "tr") String lang
    ) {
        modService.deleteCategory(idOrSlug, lang);
        return ResponseEntity.noContent().build();
    }

    // DELETE /api/mods/4
    @DeleteMapping("/mods/{id}")
    public ResponseEntity<Void> deleteMod(
            @PathVariable Long id
    ) {
        modService.deleteMod(id);
        return ResponseEntity.noContent().build();
    }
}