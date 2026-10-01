package com.example.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CevapDTO {
    private Long id;
    private Long talepId;       // İlişkili talep ID
    private String cevapMetni;
    private LocalDateTime tarih;
    private String cevaplayan;
    private String Durum ;
}
