package com.example.backend.controllers;


import com.example.backend.entity.Comment;
import com.example.backend.repositories.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*") // Angular ile haberleşebilmesi için CORS izni
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    // Belirli bir moda ait yorumları getir
    @GetMapping("/mod/{modId}")
    public List<Comment> getCommentsByMod(@PathVariable Long modId) {
        return commentRepository.findByModId(modId);
    }

    // Yeni yorum kaydet
    @PostMapping
    public Comment addComment(@RequestBody Comment comment) {
        return commentRepository.save(comment);
    }
}