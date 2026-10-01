package com.example.backend.entity;


import com.example.backend.entity.Kategori;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "destek_talepleri")
@Data
public class Talep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ad_soyad", nullable = false)
    private String adSoyad;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "konu", nullable = false)
    private String konu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "kategori_id", nullable = false)
    private Kategori kategori;

    @Column(name = "aciklama", columnDefinition = "TEXT", nullable = false)
    private String aciklama;

    @Column(name = "login", nullable = false)
    private String login;

    @Column(name = "durum", nullable = false)
    private int durum = 0;  // 0 = cevap yok, 1 = cevap var

}
