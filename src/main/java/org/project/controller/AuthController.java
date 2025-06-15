package org.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project.dto.AuthRequest;
import org.project.dto.AuthResponse;
import org.project.dto.RegisterRequest;
import org.project.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для обработки запросов аутентификации и регистрации пользователей.
 * Предоставляет REST API для входа в систему и создания новых учетных записей.
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "API для аутентификации и регистрации пользователей")
public class AuthController {

    private final AuthService authService;

    /**
     * Регистрирует нового пользователя в системе.
     *
     * @param request объект запроса с данными для регистрации
     * @return ответ с JWT токеном для аутентификации
     */
    @Operation(
            summary = "Регистрация нового пользователя",
            description = "Позволяет создать новую учетную запись пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешная регистрация",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)
                    )),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные данные запроса",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Пользователь с таким email уже существует",
                            content = @Content(schema = @Schema(hidden = true))
                    )
                    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Parameter(description = "Данные для регистрации", required = true)
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    /**
     * Аутентифицирует пользователя в системе.
     *
     * @param request объект запроса с учетными данными
     * @return ответ с JWT токеном для аутентификации
     */
    @Operation(
            summary = "Аутентификация пользователя",
            description = "Позволяет пользователю войти в систему, используя email и пароль"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Успешная аутентификация",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponse.class)
                    )),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Некорректные данные запроса",
                            content = @Content(schema = @Schema(hidden = true))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Неверные учетные данные",
                            content = @Content(schema = @Schema(hidden = true))
                    )
                    })
            @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(
            @Parameter(description = "Учетные данные для входа", required = true)
            @Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}