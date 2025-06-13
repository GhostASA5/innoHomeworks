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
import org.project.dto.ClientDTO;
import org.project.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления клиентами брокерского приложения.
 * Предоставляет REST API для работы с клиентами.
 */
@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
@Tag(name = "Clients", description = "API для управления клиентами")
public class ClientController {

    private final ClientService clientService;

    /**
     * Получает список всех клиентов.
     *
     * @return список клиентов
     */
    @Operation(summary = "Получить всех клиентов", description = "Доступно только администраторам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка клиентов",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClientDTO.class))),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    /**
     * Получает клиента по идентификатору.
     *
     * @param id идентификатор клиента
     * @return данные клиента
     */
    @Operation(summary = "Получить клиента по ID", description = "Доступно владельцу аккаунта или администратору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Клиент найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClientDTO.class))),
            @ApiResponse(responseCode = "404", description = "Клиент не найден"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @clientSecurityService.isClientOwner(#id, authentication)")
    public ResponseEntity<ClientDTO> getClientById(
            @Parameter(description = "ID клиента", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    /**
     * Создает нового клиента.
     *
     * @param clientDTO данные нового клиента
     * @return созданный клиент
     */
    @Operation(summary = "Создать нового клиента", description = "Доступно всем")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Клиент создан",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClientDTO.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные клиента")
    })
    @PostMapping
    public ResponseEntity<ClientDTO> createClient(
            @Parameter(description = "Данные клиента", required = true)
            @Valid @RequestBody ClientDTO clientDTO) {
        return new ResponseEntity<>(clientService.createClient(clientDTO), HttpStatus.CREATED);
    }

    /**
     * Обновляет данные клиента.
     *
     * @param id идентификатор клиента
     * @param clientDTO обновленные данные клиента
     * @return обновленные данные клиента
     */
    @Operation(summary = "Обновить данные клиента", description = "Доступно владельцу аккаунта или администратору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные клиента обновлены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ClientDTO.class))),
            @ApiResponse(responseCode = "404", description = "Клиент не найден"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные клиента"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @clientSecurityService.isClientOwner(#id, authentication)")
    public ResponseEntity<ClientDTO> updateClient(
            @Parameter(description = "ID клиента", required = true)
            @PathVariable Long id,
            @Parameter(description = "Обновленные данные клиента", required = true)
            @Valid @RequestBody ClientDTO clientDTO) {
        return ResponseEntity.ok(clientService.updateClient(id, clientDTO));
    }

    /**
     * Удаляет клиента (soft delete).
     *
     * @param id идентификатор клиента
     */
    @Operation(summary = "Удалить клиента", description = "Доступно владельцу аккаунта или администратору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Клиент удален"),
            @ApiResponse(responseCode = "404", description = "Клиент не найден"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @clientSecurityService.isClientOwner(#id, authentication)")
    public ResponseEntity<Void> deleteClient(
            @Parameter(description = "ID клиента", required = true)
            @PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
