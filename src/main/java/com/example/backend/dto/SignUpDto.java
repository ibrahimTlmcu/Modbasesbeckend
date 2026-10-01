// src/main/java/com/example/backend/dto/SignUpDto.java
package com.example.backend.dto;

import jakarta.validation.constraints.*;
import java.util.List;

public record SignUpDto(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String login,
        @NotBlank String password,
        @NotEmpty  List<String> roles        // ← roller eklendi
) {}
