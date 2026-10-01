package com.example.backend.repositories;

import com.example.backend.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
//bizi uzun sqlden krutardı
}