
package com.example.backend.controller;

import com.example.backend.entity.CategoryTranslation;
import com.example.backend.entity.ModItemTranslation;
import com.example.backend.repositories.CategoryTranslationRepository;
import com.example.backend.repositories.ModItemTranslationRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class SeoSitemapController {

    private final CategoryTranslationRepository categoryRepository;
    private final ModItemTranslationRepository modRepository;

    private static final String BASE = "https://www.modbases.com";
    private static final List<String> LANGS = List.of("tr", "en", "de");

    @GetMapping(value = "/api/seo/sitemap", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> sitemap() {

        StringBuilder xml = new StringBuilder();

        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\" ")
                .append("xmlns:xhtml=\"http://www.w3.org/1999/xhtml\">");

        Map<String, String> home = new LinkedHashMap<>();
        home.put("tr", BASE + "/");
        home.put("en", BASE + "/en/dashboard");
        home.put("de", BASE + "/de/dashboard");
        appendAlternates(xml, home);

        List<CategoryTranslation> categories = categoryRepository.findAll();

        Map<Long, Map<String, String>> categoryUrls = new HashMap<>();

        for (CategoryTranslation category : categories) {
            String lang = category.getLanguageCode();

            if (!LANGS.contains(lang) || !validSlug(category.getSlug())) {
                continue;
            }

            Long id = category.getCategory().getId();

            categoryUrls
                    .computeIfAbsent(id, k -> new LinkedHashMap<>())
                    .put(lang, url(lang, category.getSlug()));
        }

        for (Map<String, String> urls : categoryUrls.values()) {
            appendAlternates(xml, urls);
        }

        List<ModItemTranslation> mods = modRepository.findAll();

        Map<Long, Map<String, String>> modUrls = new HashMap<>();

        for (ModItemTranslation mod : mods) {
            String lang = mod.getLanguageCode();

            if (!LANGS.contains(lang) || !validSlug(mod.getSlug())) {
                continue;
            }

            if (mod.getModItem().getCategory() == null) {
                continue;
            }

            Long categoryId = mod.getModItem().getCategory().getId();
            String categoryUrl = categoryUrls
                    .getOrDefault(categoryId, Map.of())
                    .get(lang);

            if (categoryUrl == null) {
                continue;
            }

            Long modId = mod.getModItem().getId();

            modUrls
                    .computeIfAbsent(modId, k -> new LinkedHashMap<>())
                    .put(lang, categoryUrl + "/" + mod.getSlug());
        }

        for (Map<String, String> urls : modUrls.values()) {
            appendAlternates(xml, urls);
        }

        xml.append("</urlset>");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_XML)
                .body(xml.toString());
    }

    private String url(String language, String slug) {
        return BASE + ("tr".equals(language) ? "/" : "/" + language + "/") + slug;
    }

    private boolean validSlug(String slug) {
        return slug != null &&
                slug.matches("[a-zA-Z0-9]+(?:-[a-zA-Z0-9]+)*");
    }

    private void appendAlternates(
            StringBuilder xml,
            Map<String, String> urls
    ) {
        for (String lang : LANGS) {
            String location = urls.get(lang);

            if (location == null) {
                continue;
            }

            xml.append("<url><loc>")
                    .append(escapeXml(location))
                    .append("</loc>");

            for (String alternateLang : LANGS) {
                String alternateUrl = urls.get(alternateLang);

                if (alternateUrl != null) {
                    xml.append("<xhtml:link rel=\"alternate\" hreflang=\"")
                            .append(alternateLang)
                            .append("\" href=\"")
                            .append(escapeXml(alternateUrl))
                            .append("\"/>");
                }
            }

            xml.append("</url>");
        }
    }

    private String escapeXml(String value) {
        return value.replace("&", "&amp;")
                .replace("\"", "&quot;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("'", "&apos;");
    }
}
