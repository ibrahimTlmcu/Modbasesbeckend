package  com.example.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "kategoriler")
@Data
public class Kategori {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kategoriId;

    @Column(name = "kategoriAd", nullable = false)
    private String kategoriAd;
}
