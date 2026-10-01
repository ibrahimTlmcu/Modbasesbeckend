package com.example.backend.config;

import com.example.backend.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final UserAuthProvider userAuthenticationProvider;



    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header != null) {
            String[] authElements = header.split(" ");
            if (authElements.length == 2 && "Bearer".equals(authElements[0])) {
                try {
                    Authentication auth;
                    if ("GET".equals(request.getMethod())) {
                        auth = userAuthenticationProvider.validateToken(authElements[1]);
                    } else {
                        auth = userAuthenticationProvider.validateTokenStrongly(authElements[1]);
                    }

                    if (auth != null) {
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                    // auth null ise problem yaratma, devam etsin
                } catch (Exception e) {
                    SecurityContextHolder.clearContext();
                    System.out.println("Token doğrulama hatası: " + e.getMessage());
                    // BURADA response.sendError(401) çağırma
                    // Hata bastırılıyor, zincir devam ediyor
                }
            }
        }

        filterChain.doFilter(request, response);
    }


}