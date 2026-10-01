package com.example.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class TalepDetayResponseDTO {
    private TalepDTO talep;
    private List<CevapDTO> cevaplar;
}