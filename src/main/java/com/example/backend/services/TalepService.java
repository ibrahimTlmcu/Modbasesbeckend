package com.example.backend.services;

import com.example.backend.dto.CevapDTO;
import com.example.backend.dto.TalepDTO;
import com.example.backend.dto.TalepDetayResponseDTO;
import com.example.backend.entity.Cevap;
import com.example.backend.entity.Kategori;
import com.example.backend.entity.Talep;
import com.example.backend.repositories.CevapRepository;
import com.example.backend.repositories.KategoriRepository;
import com.example.backend.repositories.TalepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TalepService {

    @Autowired
    private TalepRepository talepRepository;
    @Autowired
    private CevapRepository cevapRepository;

    @Autowired
    private KategoriRepository kategoriRepository;

    public TalepDTO createTalep(TalepDTO talepDTO) {
        Kategori kategori = kategoriRepository.findById(talepDTO.getKategoriId())
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı, id: " + talepDTO.getKategoriId()));

        Talep talep = new Talep();
        talep.setAdSoyad(talepDTO.getAdSoyad());
        talep.setEmail(talepDTO.getEmail());
        talep.setKonu(talepDTO.getKonu());
        talep.setKategori(kategori);
        talep.setAciklama(talepDTO.getAciklama());
        talep.setLogin(talepDTO.getLogin());
        talep.setDurum(0);


        Talep savedTalep = talepRepository.save(talep);
        return mapToDTO(savedTalep);
    }

    public List<TalepDTO> getAllTalepler() {
        return talepRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public TalepDTO getTalepById(Long id) {
        Talep talep = talepRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Talep bulunamadı, id: " + id));
        return mapToDTO(talep);
    }

    public TalepDTO updateTalep(Long id, TalepDTO talepDTO) {
        Talep talep = talepRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Talep bulunamadı, id: " + id));

        Kategori kategori = kategoriRepository.findById(talepDTO.getKategoriId())
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı, id: " + talepDTO.getKategoriId()));

        talep.setAdSoyad(talepDTO.getAdSoyad());
        talep.setEmail(talepDTO.getEmail());
        talep.setKonu(talepDTO.getKonu());
        talep.setKategori(kategori);
        talep.setAciklama(talepDTO.getAciklama());

        Talep updatedTalep = talepRepository.save(talep);
        return mapToDTO(updatedTalep);
    }

    public void deleteTalep(Long id) {
        Talep talep = talepRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Talep bulunamadı, id: " + id));
        talepRepository.delete(talep);
    }
    public TalepDetayResponseDTO getTalepDetay(Long id) {
        Talep talep = talepRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Talep bulunamadı, id: " + id));

        List<Cevap> cevaplar = cevapRepository.findByTalepId(id);

        TalepDetayResponseDTO responseDTO = new TalepDetayResponseDTO();
        responseDTO.setTalep(mapToDTO(talep));
        responseDTO.setCevaplar(cevaplar.stream().map(this::mapToCevapDTO).collect(Collectors.toList()));

        return responseDTO;
    }


    public TalepDTO mapToDTO(Talep talep) {
        TalepDTO dto = new TalepDTO();
        dto.setId(talep.getId());
        dto.setAdSoyad(talep.getAdSoyad());
        dto.setEmail(talep.getEmail());
        dto.setKonu(talep.getKonu());
        dto.setAciklama(talep.getAciklama());
        dto.setLogin(talep.getLogin());

        // Kategori null olabilir, kontrol ekle
        if (talep.getKategori() != null) {
            dto.setKategoriId(talep.getKategori().getKategoriId());
        }
        dto.setDurum(Integer.parseInt(String.valueOf(talep.getDurum())));
        return dto;
    }


    private CevapDTO mapToCevapDTO(Cevap cevap) {
        CevapDTO dto = new CevapDTO();
        dto.setId(cevap.getId());
        dto.setTalepId(cevap.getTalep().getId());
        dto.setCevapMetni(cevap.getCevapMetni());
        dto.setTarih(cevap.getTarih());
        dto.setCevaplayan(cevap.getCevaplayan());

        return dto;
    }


    public List<Talep> getTaleplerByLogin(String login) {
        return talepRepository.findByLogin(login);
    }

}
