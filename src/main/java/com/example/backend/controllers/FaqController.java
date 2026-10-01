package com.example.backend.controllers;

import com.example.backend.entity.Faq;
import com.example.backend.services.FaqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faqs")
@CrossOrigin(origins = "http://localhost:4200") // Angular'ın varsayılan portu
public class FaqController {
    @Autowired
    private FaqService faqService;

    @GetMapping
    public List<Faq> getAllFaqs() {
        return faqService.getAllFaqs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faq> getFaqById(@PathVariable Long id) {
        return faqService.getFaqById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Faq createFaq(@RequestBody Faq faq) {
        return faqService.createFaq(faq);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Faq> updateFaq(@PathVariable Long id, @RequestBody Faq faqDetails) {
        return ResponseEntity.ok(faqService.updateFaq(id, faqDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaq(@PathVariable Long id) {
        faqService.deleteFaq(id);
        return ResponseEntity.noContent().build();
    }
}