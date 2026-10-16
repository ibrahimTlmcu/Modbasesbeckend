package com.example.backend.repositories;


import com.example.backend.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Belirli bir modun ID'sine göre yorumları listeleyen metot
    List<Comment> findByModId(Long modId);
}