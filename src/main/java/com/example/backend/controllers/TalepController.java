package com.example.backend.controllers;

import com.example.backend.dto.TalepDTO;
import com.example.backend.dto.TalepDetayResponseDTO;
import com.example.backend.entity.Cevap;
import com.example.backend.entity.Talep;
import com.example.backend.entity.User;
import com.example.backend.repositories.CevapRepository;
import com.example.backend.repositories.TalepRepository;
import com.example.backend.services.CurrentUserService;
import com.example.backend.services.TalepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/talepler")
@CrossOrigin(origins = "http://localhost:4200")
public class TalepController {
    private final CurrentUserService currentUserService;
    @Autowired
    private TalepService talepService;

    public TalepController(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }


    @PostMapping
    public ResponseEntity<TalepDTO> createTalep(@RequestBody TalepDTO talepDTO) {
        try {
            String currentUserLogin = currentUserService.getCurrentLogin();
            talepDTO.setLogin(currentUserLogin); // buraya direkt string ver

            TalepDTO createdTalep = talepService.createTalep(talepDTO);
            return ResponseEntity.status(201).body(createdTalep);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }


    @GetMapping
    public ResponseEntity<List<TalepDTO>> getAllTalepler() {
        return ResponseEntity.ok(talepService.getAllTalepler());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TalepDTO> getTalepById(@PathVariable Long id) {
        try {
            TalepDTO talep = talepService.getTalepById(id);
            return ResponseEntity.ok(talep);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TalepDTO> updateTalep(@PathVariable Long id, @RequestBody TalepDTO talepDTO) {
        try {
            TalepDTO updatedTalep = talepService.updateTalep(id, talepDTO);
            return ResponseEntity.ok(updatedTalep);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTalep(@PathVariable Long id) {
        try {
            talepService.deleteTalep(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    // Talep + Cevap detayları


    @GetMapping("/{id}/detay")
    public ResponseEntity<TalepDetayResponseDTO> getTalepDetay(@PathVariable Long id) {
        try {
            TalepDetayResponseDTO detay = talepService.getTalepDetay(id);
            return ResponseEntity.ok(detay);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/kullanici/{login}")
    public ResponseEntity<List<TalepDTO>> getTaleplerByLogin(@PathVariable String login) {
        List<TalepDTO> taleplerDto = talepService.getTaleplerByLogin(login)
                .stream()
                .map(talep -> talepService.mapToDTO(talep))
                .toList();

        return ResponseEntity.ok(taleplerDto);
    }


}