package com.example.backend.controllers;

import com.example.backend.dto.CevapDTO;
import com.example.backend.services.CevapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cevaplar")
@CrossOrigin(origins = "http://localhost:4200")
public class CevapController {

    @Autowired
    private CevapService cevapService;

    // Belirli talebe ait cevapları listele
    @GetMapping("/talep/{talepId}")
    public ResponseEntity<List<CevapDTO>> getCevaplarByTalepId(@PathVariable Long talepId) {
        List<CevapDTO> cevaplar = cevapService.getCevaplarByTalepId(talepId);
        return ResponseEntity.ok(cevaplar);
    }

    // Talebe yeni cevap ekle
    @PostMapping("/talep/{talepId}")
    public ResponseEntity<CevapDTO> addCevap(@PathVariable Long talepId, @RequestBody CevapDTO cevapDTO) {
        try {
            CevapDTO createdCevap = cevapService.addCevapToTalep(talepId, cevapDTO);
            return ResponseEntity.status(201).body(createdCevap);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Cevap sil
    @DeleteMapping("/{cevapId}")
    public ResponseEntity<Void> deleteCevap(@PathVariable Long cevapId) {
        cevapService.deleteCevap(cevapId);
        return ResponseEntity.noContent().build();
    }
}
