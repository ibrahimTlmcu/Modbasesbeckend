package com.example.backend.repositories;

import com.example.backend.entity.Talep;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TalepRepository extends JpaRepository<Talep, Long> {
    List<Talep> findByLogin(String login);
}
