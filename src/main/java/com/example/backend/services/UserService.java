package com.example.backend.services;

import com.example.backend.dto.CredentialsDto;
import com.example.backend.dto.PasswordUpdateDto;
import com.example.backend.dto.SignUpDto;
import com.example.backend.dto.UserDto;
import com.example.backend.entity.User;
import com.example.backend.exceptions.AppExceptions;
import com.example.backend.mappers.UserMapper;
import com.example.backend.repositories.UserRepositories;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepositories userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    // src/main/java/com/example/backend/services/UserService.java
    public UserDto login(CredentialsDto credentials) {
        User user = userRepository.findByLogin(credentials.login())
                .orElseThrow(() -> new AppExceptions("User not found", HttpStatus.NOT_FOUND));

        if (!passwordEncoder.matches(
                String.valueOf(credentials.password()),
                user.getPassword())) {
            throw new AppExceptions("Invalid credentials", HttpStatus.UNAUTHORIZED);
        }

        // DTO’ya rollerini taşı
        UserDto dto = UserDto.builder()
                .id(user.getId())
                .login(user.getLogin())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(new ArrayList<>(user.getRoles()))   // ← burada set ediliyor
                .build();

        return dto;
    }



    public UserDto register(SignUpDto userDto) {
        if (userRepository.findByLogin(userDto.login()).isPresent()) {
            throw new AppExceptions("Login already exists", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.signUpToUser(userDto);
        // eski CharBuffer.wrap(...) yerine direkt:
        user.setPassword(passwordEncoder.encode(userDto.password()));
        // DEFAULT rol ataması, eğer henüz yapmadıysanız:
        user.getRoles().add("USER");
        user = userRepository.save(user);

        return userMapper.toUserDto(user);
    }



    public UserDto findByLogin(String login) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new AppExceptions("Unknown user", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }


    public void updatePassword(Long userId, PasswordUpdateDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Eski şifre yanlış");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
    }
}