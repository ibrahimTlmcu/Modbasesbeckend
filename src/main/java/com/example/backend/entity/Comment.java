package com.example.backend.entity; // Kendi paket adınıza göre değiştirebilirsiniz

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long modId; // Hangi moda yapıldığı (Modun ID'si)
    private String userName;
    private String userEmail;

    @Column(length = 2000)
    private String text;

    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters ve Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getModId() { return modId; }
    public void setModId(Long modId) { this.modId = modId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}