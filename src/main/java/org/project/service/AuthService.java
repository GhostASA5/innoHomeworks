package org.project.service;

import lombok.RequiredArgsConstructor;
import org.project.dto.AuthRequest;
import org.project.dto.AuthResponse;
import org.project.dto.RegisterRequest;
import org.project.model.Client;
import org.project.model.Role;
import org.project.repository.ClientRepository;
import org.project.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Сервис для аутентификации и регистрации пользователей.
 * Обеспечивает процессы входа в систему и создания новых учетных записей.
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Регистрирует нового пользователя в системе.
     *
     * @param request объект запроса с данными для регистрации
     * @return объект ответа с JWT токеном для аутентификации
     */
    public AuthResponse register(RegisterRequest request) {
        var client = Client.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .deleted(false)
                .build();

        clientRepository.save(client);

        var jwtToken = jwtService.generateToken(client);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }

    /**
     * Аутентифицирует пользователя в системе.
     *
     * @param request объект запроса с учетными данными (email и пароль)
     * @return объект ответа с JWT токеном для аутентификации
     * @throws org.springframework.security.core.AuthenticationException если аутентификация не удалась
     */
    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var client = clientRepository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(client);
        return AuthResponse.builder()
                .token(jwtToken)
                .build();
    }
}