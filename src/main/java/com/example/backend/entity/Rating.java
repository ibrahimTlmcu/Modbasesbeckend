package com.example.backend.entity;// src/main/java/com/alibou/websocket/data/Rating.java

import jakarta.persistence.*;
import lombok.*;
;

@Entity
@Table(name = "ratings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ticketId;
    private String rater;    // puanı veren (user veya expert)
    private String rated;    // puanı alan (expert veya user)
    private int score;       // 1–5 arası
    private String comment;  // isteğe bağlı
}
