package com.example.backend.services;

import com.example.backend.dto.CevapDTO;
import com.example.backend.entity.Cevap;
import com.example.backend.entity.Talep;
import com.example.backend.repositories.CevapRepository;
import com.example.backend.repositories.TalepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CevapService {

    @Autowired
    private CevapRepository cevapRepository;

    @Autowired
    private TalepRepository talepRepository;

    // Talep ID'ye göre cevapları getir
    public List<CevapDTO> getCevaplarByTalepId(Long talepId) {
        List<Cevap> cevaplar = cevapRepository.findByTalepId(talepId);
        return cevaplar.stream()
                .map(this::mapCevapToDTO)
                .collect(Collectors.toList());
    }

    // Talebe yeni cevap ekle
    public CevapDTO addCevapToTalep(Long talepId, CevapDTO cevapDTO) {
        Talep talep = talepRepository.findById(talepId)
                .orElseThrow(() -> new RuntimeException("Talep bulunamadı, id: " + talepId));

        Cevap cevap = new Cevap();
        cevap.setTalep(talep);
        cevap.setCevapMetni(cevapDTO.getCevapMetni());
        cevap.setCevaplayan(cevapDTO.getCevaplayan());
        cevap.setTarih(LocalDateTime.now());
        talep.setDurum(1);
        Cevap kaydedilen = cevapRepository.save(cevap);
        return mapCevapToDTO(kaydedilen);
    }

    // Cevap sil
    public void deleteCevap(Long cevapId) {
        cevapRepository.deleteById(cevapId);
    }

    // Entity → DTO dönüşümü
    private CevapDTO mapCevapToDTO(Cevap cevap) {
        CevapDTO dto = new CevapDTO();
        dto.setId(cevap.getId());
        dto.setTalepId(cevap.getTalep().getId());
        dto.setCevapMetni(cevap.getCevapMetni());
        dto.setCevaplayan(cevap.getCevaplayan());
        dto.setTarih(cevap.getTarih());
        return dto;
    }
}
