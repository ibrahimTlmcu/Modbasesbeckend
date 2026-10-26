
        package com.example.backend.services;

import com.example.backend.dto.*;
import com.example.backend.entity.*;
import com.example.backend.repositories.CategoryRepository;
import com.example.backend.repositories.CategoryTranslationRepository;
import com.example.backend.repositories.ModItemRepository;
import com.example.backend.repositories.ModItemTranslationRepository;
import com.example.backend.service.TranslationService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class ModService {

    private final CategoryRepository categoryRepository;
    private final ModItemRepository modItemRepository;
    private final CategoryTranslationRepository categoryTranslationRepository;
    private final ModItemTranslationRepository modItemTranslationRepository;
    private final TranslationService translationService;

    private static final Set<String> LANGUAGES = Set.of("tr", "en", "de");

    // ==================== DİL ====================

    private String normalizeLanguage(String language) {
        String normalized = language == null || language.isBlank()
                ? "tr"
                : language.trim().toLowerCase(Locale.ROOT);

        if (!LANGUAGES.contains(normalized)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Desteklenmeyen dil: " + normalized
            );
        }

        return normalized;
    }

    private String validateTranslationLanguage(String language) {
        if (language == null || language.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Çeviri için languageCode zorunludur."
            );
        }

        return normalizeLanguage(language);
    }

    private void requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    field + " boş bırakılamaz."
            );
        }
    }

    private void validateTranslations(List<?> translations) {
        if (translations == null || translations.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "En az bir dil çevirisi göndermelisiniz."
            );
        }
    }

    private String localizedSlug(String sourceSlug, String language) {
        // Mevcut TR slug'ını koru; dil bazında benzersiz URL üret.
        return sourceSlug.trim().toLowerCase(Locale.ROOT) + "-" + language;
    }

    // ==================== KATEGORİ BULMA ====================

    private Optional<CategoryTranslation> findCategoryTranslation(
            String idOrSlug,
            String language
    ) {
        if (idOrSlug == null || idOrSlug.isBlank()) {
            return Optional.empty();
        }

        if (idOrSlug.matches("\\d+")) {
            Optional<CategoryTranslation> byId =
                    categoryRepository.findById(Long.parseLong(idOrSlug))
                            .flatMap(category ->
                                    categoryTranslationRepository
                                            .findByCategory_IdAndLanguageCode(
                                                    category.getId(),
                                                    language
                                            )
                            );

            if (byId.isPresent()) {
                return byId;
            }
        }

        return categoryTranslationRepository
                .findByLanguageCodeAndSlug(language, idOrSlug);
    }

    private Optional<Category> findCategoryEntity(
            String idOrSlug,
            String language
    ) {
        if (idOrSlug == null || idOrSlug.isBlank()) {
            return Optional.empty();
        }

        if (idOrSlug.matches("\\d+")) {
            Optional<Category> byId =
                    categoryRepository.findById(Long.parseLong(idOrSlug));

            if (byId.isPresent()) {
                return byId;
            }
        }

        return categoryTranslationRepository
                .findByLanguageCodeAndSlug(language, idOrSlug)
                .map(CategoryTranslation::getCategory);
    }

    // ==================== RESPONSE MAPPING ====================

    private CategoryResponse toCategoryResponse(
            CategoryTranslation translation
    ) {
        Category category = translation.getCategory();

        return new CategoryResponse(
                category.getId(),
                translation.getMainGroup(),
                translation.getName(),
                translation.getSlug(),
                translation.getHeaderTitle(),
                translation.getDescription(),
                translation.getBackLinkText(),
                translation.getAboutTitle()
        );
    }

    private ModItemResponse toModItemResponse(
            ModItemTranslation translation,
            String language
    ) {
        ModItem item = translation.getModItem();

        CategoryResponse categoryResponse = null;

        if (item.getCategory() != null) {
            categoryResponse = categoryTranslationRepository
                    .findByCategory_IdAndLanguageCode(
                            item.getCategory().getId(),
                            language
                    )
                    .map(this::toCategoryResponse)
                    .orElse(null);
        }

        return new ModItemResponse(
                item.getId(),
                translation.getTitle(),
                translation.getSlug(),
                item.getThumbnailUrl(),
                translation.getShortDescription(),
                translation.getBadgeText(),
                translation.getGallery(),
                translation.getAboutDescription(),
                translation.getFeatures(),
                translation.getDownloadUrl(),
                translation.getDownloadBtnText(),
                translation.getInstallNote(),
                translation.getVideoSectionTitle(),
                translation.getVideoDescription(),
                translation.getYoutubeEmbedUrl(),
                translation.getIframeTitle(),
                categoryResponse
        );
    }

    // ==================== KATEGORİ OKUMA ====================

    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories(String language) {
        String lang = normalizeLanguage(language);

        return categoryTranslationRepository
                .findByLanguageCodeOrderByNameAsc(lang)
                .stream()
                .map(this::toCategoryResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<CategoryResponse> getCategory(
            String idOrSlug,
            String language
    ) {
        String lang = normalizeLanguage(language);

        return findCategoryTranslation(idOrSlug, lang)
                .map(this::toCategoryResponse);
    }

    // ==================== MOD OKUMA ====================

    @Transactional(readOnly = true)
    public List<ModItemResponse> getModsByCategory(
            String categoryIdOrSlug,
            String language
    ) {
        String lang = normalizeLanguage(language);

        Optional<CategoryTranslation> category =
                findCategoryTranslation(categoryIdOrSlug, lang);

        if (category.isEmpty()) {
            return List.of();
        }

        Long categoryId = category.get().getCategory().getId();

        return modItemTranslationRepository
                .findByLanguageCodeAndModItem_Category_IdOrderByTitleAsc(
                        lang,
                        categoryId
                )
                .stream()
                .map(translation -> toModItemResponse(translation, lang))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ModItemResponse> getAllMods(String language) {
        String lang = normalizeLanguage(language);

        return modItemTranslationRepository
                .findByLanguageCodeOrderByTitleAsc(lang)
                .stream()
                .map(translation -> toModItemResponse(translation, lang))
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<ModItemResponse> getModDetail(
            String categoryIdOrSlug,
            String modSlug,
            String language
    ) {
        String lang = normalizeLanguage(language);

        Optional<CategoryTranslation> category =
                findCategoryTranslation(categoryIdOrSlug, lang);

        if (category.isEmpty()) {
            return Optional.empty();
        }

        Long categoryId = category.get().getCategory().getId();

        return modItemTranslationRepository
                .findByLanguageCodeAndSlugAndModItem_Category_Id(
                        lang,
                        modSlug,
                        categoryId
                )
                .map(translation -> toModItemResponse(translation, lang));
    }

    // ==================== KATEGORİ OLUŞTURMA ====================

    public CategoryResponse createCategory(
            CategoryRequest request,
            String language
    ) {
        if (request == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Kategori verisi zorunludur."
            );
        }

        validateTranslations(request.translations());

        Category category = new Category();
        List<CategoryTranslation> translations = new ArrayList<>();
        Set<String> usedLanguages = new HashSet<>();

        for (CategoryTranslationRequest input : request.translations()) {
            if (input == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Kategori çevirisi boş olamaz."
                );
            }

            String lang = validateTranslationLanguage(input.languageCode());

            if (!usedLanguages.add(lang)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Aynı kategori içinde aynı dil birden fazla kez kullanılamaz: "
                                + lang
                );
            }

            requireText(input.name(), "Kategori adı");
            requireText(input.slug(), "Kategori slug");

            CategoryTranslation translation = new CategoryTranslation();
            translation.setCategory(category);
            translation.setLanguageCode(lang);
            translation.setName(input.name().trim());
            translation.setSlug(input.slug().trim());
            translation.setMainGroup(input.mainGroup());
            translation.setHeaderTitle(input.headerTitle());
            translation.setDescription(input.description());
            translation.setBackLinkText(input.backLinkText());
            translation.setAboutTitle(input.aboutTitle());

            translations.add(translation);
        }

        // Sadece Türkçe gönderilmişse eksik EN/DE çevirilerini oluştur.
        CategoryTranslationRequest trCategory = request.translations().stream()
                .filter(t -> t != null && "tr".equalsIgnoreCase(t.languageCode()))
                .findFirst().orElse(null);
        if (trCategory != null) {
            for (String target : List.of("en", "de")) {
                if (usedLanguages.contains(target)) continue;
                CategoryTranslation translated = new CategoryTranslation();
                translated.setCategory(category);
                translated.setLanguageCode(target);
                translated.setName(translationService.translate(trCategory.name(), target));
                translated.setSlug(localizedSlug(trCategory.slug(), target));
                translated.setMainGroup(translationService.translate(trCategory.mainGroup(), target));
                translated.setHeaderTitle(translationService.translate(trCategory.headerTitle(), target));
                translated.setDescription(translationService.translate(trCategory.description(), target));
                translated.setBackLinkText(translationService.translate(trCategory.backLinkText(), target));
                translated.setAboutTitle(translationService.translate(trCategory.aboutTitle(), target));
                translations.add(translated);
            }
        }

        category.setTranslations(translations);
        Category saved = categoryRepository.save(category);

        String responseLanguage = normalizeLanguage(language);

        return saved.getTranslations()
                .stream()
                .filter(t -> t.getLanguageCode().equals(responseLanguage))
                .findFirst()
                .or(() -> saved.getTranslations().stream().findFirst())
                .map(this::toCategoryResponse)
                .orElseThrow();
    }

    // ==================== MOD OLUŞTURMA ====================

    public ModItemResponse createModByCategory(
            String categoryIdOrSlug,
            ModItemRequest request,
            String language
    ) {
        String lang = normalizeLanguage(language);

        Category category = findCategoryTranslation(
                categoryIdOrSlug,
                lang
        ).map(CategoryTranslation::getCategory).orElseThrow(() ->
                new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Seçilen dilde kategori bulunamadı."
                )
        );

        return createMod(category, request, lang);
    }

    public ModItemResponse createModDirect(
            ModItemRequest request,
            String language
    ) {
        if (request == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Mod verisi zorunludur."
            );
        }

        String lang = normalizeLanguage(language);
        Category category;

        if (request.categoryId() != null) {
            category = categoryRepository.findById(request.categoryId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Kategori bulunamadı."
                    ));
        } else if (request.categorySlug() != null
                && !request.categorySlug().isBlank()) {
            category = findCategoryEntity(request.categorySlug(), lang)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Kategori bulunamadı."
                    ));
        } else {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "categoryId veya categorySlug zorunludur."
            );
        }

        return createMod(category, request, lang);
    }

    private ModItemResponse createMod(
            Category category,
            ModItemRequest request,
            String responseLanguage
    ) {
        if (request == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Mod verisi zorunludur."
            );
        }

        validateTranslations(request.translations());

        ModItem item = new ModItem();
        item.setCategory(category);
        item.setThumbnailUrl(request.thumbnailUrl());

        List<ModItemTranslation> translations = new ArrayList<>();
        Set<String> usedLanguages = new HashSet<>();

        for (ModItemTranslationRequest input : request.translations()) {
            if (input == null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Mod çevirisi boş olamaz."
                );
            }

            String lang = validateTranslationLanguage(input.languageCode());

            if (!usedLanguages.add(lang)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Aynı mod içinde aynı dil birden fazla kez kullanılamaz: "
                                + lang
                );
            }

            requireText(input.title(), "Mod başlığı");
            requireText(input.slug(), "Mod slug");

            ModItemTranslation translation = new ModItemTranslation();
            translation.setModItem(item);
            translation.setLanguageCode(lang);
            translation.setTitle(input.title().trim());
            translation.setSlug(input.slug().trim());
            translation.setShortDescription(input.shortDescription());
            translation.setBadgeText(input.badgeText());
            translation.setGallery(
                    input.gallery() == null
                            ? new ArrayList<>()
                            : new ArrayList<>(input.gallery())
            );
            translation.setAboutDescription(input.aboutDescription());
            translation.setFeatures(
                    input.features() == null
                            ? new ArrayList<>()
                            : new ArrayList<>(input.features())
            );
            translation.setDownloadUrl(input.downloadUrl());
            translation.setDownloadBtnText(input.downloadBtnText());
            translation.setInstallNote(input.installNote());
            translation.setVideoSectionTitle(input.videoSectionTitle());
            translation.setVideoDescription(input.videoDescription());
            translation.setYoutubeEmbedUrl(input.youtubeEmbedUrl());
            translation.setIframeTitle(input.iframeTitle());

            translations.add(translation);
        }

        // Türkçe içerikten eksik EN/DE çevirilerini otomatik hazırla.
        ModItemTranslationRequest trMod = request.translations().stream()
                .filter(t -> t != null && "tr".equalsIgnoreCase(t.languageCode()))
                .findFirst().orElse(null);
        if (trMod != null) {
            for (String target : List.of("en", "de")) {
                if (usedLanguages.contains(target)) continue;
                ModItemTranslation translated = new ModItemTranslation();
                translated.setModItem(item);
                translated.setLanguageCode(target);
                translated.setTitle(translationService.translate(trMod.title(), target));
                translated.setSlug(localizedSlug(trMod.slug(), target));
                translated.setShortDescription(translationService.translate(trMod.shortDescription(), target));
                translated.setBadgeText(translationService.translate(trMod.badgeText(), target));
                translated.setGallery(trMod.gallery() == null
                        ? new ArrayList<>() : new ArrayList<>(trMod.gallery()));
                translated.setAboutDescription(translationService.translate(trMod.aboutDescription(), target));
                List<String> translatedFeatures = new ArrayList<>();
                if (trMod.features() != null) {
                    for (String feature : trMod.features()) {
                        translatedFeatures.add(translationService.translate(feature, target));
                    }
                }
                translated.setFeatures(translatedFeatures);
                translated.setDownloadUrl(trMod.downloadUrl());
                translated.setDownloadBtnText(translationService.translate(trMod.downloadBtnText(), target));
                translated.setInstallNote(translationService.translate(trMod.installNote(), target));
                translated.setVideoSectionTitle(translationService.translate(trMod.videoSectionTitle(), target));
                translated.setVideoDescription(translationService.translate(trMod.videoDescription(), target));
                translated.setYoutubeEmbedUrl(trMod.youtubeEmbedUrl());
                translated.setIframeTitle(translationService.translate(trMod.iframeTitle(), target));
                translations.add(translated);
            }
        }

        item.setTranslations(translations);

        ModItem saved = modItemRepository.save(item);

        return saved.getTranslations()
                .stream()
                .filter(t -> t.getLanguageCode().equals(responseLanguage))
                .findFirst()
                .or(() -> saved.getTranslations().stream().findFirst())
                .map(t -> toModItemResponse(t, responseLanguage))
                .orElseThrow();
    }

    // ==================== SİLME ====================

    public void deleteCategory(String idOrSlug, String language) {
        String lang = normalizeLanguage(language);

        Category category = findCategoryEntity(idOrSlug, lang)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Kategori bulunamadı."
                ));

        // Category entity'sindeki cascade ayarı, ilişkili modları da silebilir.
        categoryRepository.delete(category);
    }

    public void deleteMod(Long id) {
        ModItem item = modItemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Mod bulunamadı."
                ));

        modItemRepository.delete(item);
    }
}
