package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// src/main/java/com/example/backend/dto/UserDto.java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String login;

    @Builder.Default                // ← Lombok Builder da default atar
    private List<String> roles = new ArrayList<>();

    private String token;
}
