package com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "taleplere_cevaplar")
@Data
public class Cevap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "talep_id", nullable = false)
    private Talep talep;

    @Column(name = "cevap_metni", columnDefinition = "TEXT", nullable = false)
    private String cevapMetni;

    @Column(name = "tarih", nullable = false)
    private LocalDateTime tarih;

    @Column(name = "cevaplayan", nullable = false)
    private String cevaplayan;

}
