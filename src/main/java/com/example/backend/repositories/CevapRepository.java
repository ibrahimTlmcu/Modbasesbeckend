package com.example.backend.repositories;

import com.example.backend.entity.Cevap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CevapRepository extends JpaRepository<Cevap, Long> {
    List<Cevap> findByTalepId(Long talepId);

}
