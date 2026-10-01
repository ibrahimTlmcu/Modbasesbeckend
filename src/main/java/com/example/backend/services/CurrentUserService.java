package com.example.backend.services;

import com.example.backend.dto.UserDto;
import com.example.backend.entity.User;
import com.example.backend.repositories.UserRepositories;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final UserRepositories userRepository;

    public CurrentUserService(UserRepositories userRepository) {
        this.userRepository = userRepository;
    }

    public String getCurrentLogin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDto) {
            UserDto userDto = (UserDto) principal;
            return userDto.getLogin();  // login alanını döner
        }

        // principal string ise (default)
        if (principal instanceof String) {
            return (String) principal;
        }

        // Beklenmeyen durum
        return null;
    }

}
