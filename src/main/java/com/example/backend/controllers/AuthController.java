package com.example.backend.controllers;
import com.example.backend.config.UserAuthProvider;
import jakarta.validation.Valid;

import com.example.backend.config.UserAuthenticationProvider;
import com.example.backend.dto.CredentialsDto;
import com.example.backend.dto.SignUpDto;
import com.example.backend.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.backend.services.UserService;
import java.net.URI;

@RequiredArgsConstructor
@RestController

public class AuthController {

    private final UserService userService;


    private final UserAuthProvider userAuthProvider;


    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody @Valid CredentialsDto credentialsDto) {
        UserDto dto = userService.login(credentialsDto);
        // userService.login döndüğü UserDto içinde artık roller de var:
        dto.setToken(userAuthProvider.createToken(dto));
        return ResponseEntity.ok(dto);
    }


    @CrossOrigin(origins = "http://localhost:4200")
    // src/main/java/com/example/backend/controllers/AuthController.java
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Valid SignUpDto dto) {
        // 1) Service katmanını rollerle çağır
        UserDto createdUser = userService.register(dto);

        // 2) JWT token’ı oluşturup DTO’ya set et
        createdUser.setRoles(dto.roles());  // DTO’ya da roller ekle
        createdUser.setToken(userAuthProvider.createToken(createdUser));

        URI location = URI.create("/users/" + createdUser.getId());
        return ResponseEntity.created(location).body(createdUser);
    }

    @PostMapping("/deneme")
    public ResponseEntity<UserDto> deneme(@RequestBody @Valid SignUpDto user) {
        UserDto createdUser = userService.register(user);
        createdUser.setToken(userAuthProvider.createToken(createdUser));
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
    }


}