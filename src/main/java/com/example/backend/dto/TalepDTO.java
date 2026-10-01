package com.example.backend.dto;

import lombok.Data;

@Data
public class TalepDTO {
    private Long id;
    private String adSoyad;
    private String email;
    private String konu;
    private Long kategoriId; // Kategori nesnesi yerine ID alıyoruz
    private String aciklama;
    private String login;
    private int durum ;

}