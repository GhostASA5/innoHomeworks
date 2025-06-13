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
import org.project.dto.BrokerAccountDTO;
import org.project.service.BrokerAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления брокерскими счетами.
 * Предоставляет REST API для работы с брокерскими счетами клиентов.
 */
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "Broker Accounts", description = "API для управления брокерскими счетами")
public class BrokerAccountController {

    private final BrokerAccountService brokerAccountService;

    /**
     * Получает список всех брокерских счетов.
     *
     * @return список брокерских счетов
     */
    @Operation(summary = "Получить все брокерские счета", description = "Доступно только администраторам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка счетов",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BrokerAccountDTO.class))),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BrokerAccountDTO>> getAllBrokerAccounts() {
        return ResponseEntity.ok(brokerAccountService.getAllBrokerAccounts());
    }

    /**
     * Получает брокерский счет по идентификатору.
     *
     * @param id идентификатор счета
     * @return данные брокерского счета
     */
    @Operation(summary = "Получить брокерский счет по ID", description = "Доступно владельцу счета или администратору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Счет найден",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BrokerAccountDTO.class))),
            @ApiResponse(responseCode = "404", description = "Счет не найден"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @clientSecurityService.isAccountOwner(#id, authentication)")
    public ResponseEntity<BrokerAccountDTO> getBrokerAccountById(
            @Parameter(description = "ID брокерского счета", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(brokerAccountService.getBrokerAccountById(id));
    }

    /**
     * Получает список брокерских счетов по идентификатору клиента.
     *
     * @param clientId идентификатор клиента
     * @return список брокерских счетов
     */
    @Operation(summary = "Получить счета клиента", description = "Доступно владельцу аккаунта или администратору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Счета найдены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BrokerAccountDTO.class))),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasRole('ADMIN') or @clientSecurityService.isClientOwner(#clientId, authentication)")
    public ResponseEntity<List<BrokerAccountDTO>> getBrokerAccountsByClientId(
            @Parameter(description = "ID клиента", required = true)
            @PathVariable Long clientId) {
        return ResponseEntity.ok(brokerAccountService.getBrokerAccountsByClientId(clientId));
    }

    /**
     * Создает новый брокерский счет.
     *
     * @param brokerAccountDTO данные нового счета
     * @return созданный брокерский счет
     */
    @Operation(summary = "Создать новый брокерский счет", description = "Доступно владельцу аккаунта или администратору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Счет создан",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BrokerAccountDTO.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные счета"),
            @ApiResponse(responseCode = "404", description = "Клиент не найден"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or @clientSecurityService.isClientOwner(#brokerAccountDTO.clientId, authentication)")
    public ResponseEntity<BrokerAccountDTO> createBrokerAccount(
            @Parameter(description = "Данные брокерского счета", required = true)
            @Valid @RequestBody BrokerAccountDTO brokerAccountDTO) {
        return new ResponseEntity<>(brokerAccountService.createBrokerAccount(brokerAccountDTO), HttpStatus.CREATED);
    }

    /**
     * Обновляет данные брокерского счета.
     *
     * @param id идентификатор счета
     * @param brokerAccountDTO обновленные данные счета
     * @return обновленные данные счета
     */
    @Operation(summary = "Обновить брокерский счет", description = "Доступно владельцу счета или администратору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Счет обновлен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BrokerAccountDTO.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные счета"),
            @ApiResponse(responseCode = "404", description = "Счет или клиент не найдены"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @clientSecurityService.isAccountOwner(#id, authentication)")
    public ResponseEntity<BrokerAccountDTO> updateBrokerAccount(
            @Parameter(description = "ID брокерского счета", required = true)
            @PathVariable Long id,
            @Parameter(description = "Обновленные данные счета", required = true)
            @Valid @RequestBody BrokerAccountDTO brokerAccountDTO) {
        return ResponseEntity.ok(brokerAccountService.updateBrokerAccount(id, brokerAccountDTO));
    }

    /**
     * Удаляет брокерский счет (soft delete).
     *
     * @param id идентификатор счета
     */
    @Operation(summary = "Удалить брокерский счет", description = "Доступно владельцу счета или администратору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Счет удален"),
            @ApiResponse(responseCode = "404", description = "Счет не найден"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @clientSecurityService.isAccountOwner(#id, authentication)")
    public ResponseEntity<Void> deleteBrokerAccount(
            @Parameter(description = "ID брокерского счета", required = true)
            @PathVariable Long id) {
        brokerAccountService.deleteBrokerAccount(id);
        return ResponseEntity.noContent().build();
    }
}