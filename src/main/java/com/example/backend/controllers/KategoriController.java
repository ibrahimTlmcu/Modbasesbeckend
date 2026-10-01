package com.example.backend.controllers;

import com.example.backend.entity.Kategori;
import com.example.backend.services.KategoriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/kategori")
@CrossOrigin(origins = "http://localhost:4200")
public class KategoriController {
    @Autowired
    private KategoriService kategoriService;

    @GetMapping
    public ResponseEntity<List<Kategori>> getAllKategori() {
        return   ResponseEntity.ok(kategoriService.getAllKategori());

    }

    @GetMapping("/{id}")
    public ResponseEntity<Kategori> getByIdKategori(@PathVariable Long id) {
        return kategoriService.getKategoriById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Kategori> createKategori(@RequestBody Kategori kategori) {
        Kategori createdKategori = kategoriService.createKategori(kategori);
        return ResponseEntity.status(201).body(createdKategori);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Kategori> updateKategori(@PathVariable Long id, @RequestBody Kategori kategoriDetails) {
        try {
            Kategori updatedKategori = kategoriService.updateKategori(id, kategoriDetails);
            return ResponseEntity.ok(updatedKategori);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteKategori(@PathVariable Long id) {
        try {
            kategoriService.deleteKategori(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}