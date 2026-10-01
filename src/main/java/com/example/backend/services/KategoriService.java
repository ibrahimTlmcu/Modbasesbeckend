package com.example.backend.services;

import com.example.backend.entity.Kategori;
import com.example.backend.repositories.KategoriRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KategoriService {
    @Autowired
    private KategoriRepository kategoriRepository;

    public List<Kategori> getAllKategori() {
        var a =  kategoriRepository.findAll();
        return a ;
    }

    public Optional<Kategori> getKategoriById(Long id) {
        return kategoriRepository.findById(id);
    }

    public Kategori createKategori(Kategori kategori) {
        return kategoriRepository.save(kategori);
    }

    public Kategori updateKategori(Long id, Kategori kategoriDetails) {
        Kategori kategori = kategoriRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı, id: " + id));
        kategori.setKategoriAd(kategoriDetails.getKategoriAd());
        return kategoriRepository.save(kategori);
    }

    public void deleteKategori(Long id) { // Metod adı düzeltildi: deleterKategori -> deleteKategori
        Kategori kategori = kategoriRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı, id: " + id));
        kategoriRepository.delete(kategori);
    }
}