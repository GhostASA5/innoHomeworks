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
import org.project.dto.TransactionDTO;
import org.project.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления транзакциями брокерских счетов.
 * Предоставляет REST API для работы с финансовыми транзакциями.
 */
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transactions", description = "API для управления транзакциями")
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Получает список всех транзакций.
     *
     * @return список транзакций
     */
    @Operation(summary = "Получить все транзакции", description = "Доступно только администраторам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное получение списка транзакций",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionDTO.class))),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    /**
     * Получает транзакцию по идентификатору.
     *
     * @param id идентификатор транзакции
     * @return данные транзакции
     */
    @Operation(summary = "Получить транзакцию по ID", description = "Доступно владельцу счета или администратору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Транзакция найдена",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Транзакция не найдена"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @clientSecurityService.isTransactionOwner(#id, authentication)")
    public ResponseEntity<TransactionDTO> getTransactionById(
            @Parameter(description = "ID транзакции", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    /**
     * Получает список транзакций по идентификатору брокерского счета.
     *
     * @param brokerAccountId идентификатор брокерского счета
     * @return список транзакций
     */
    @Operation(summary = "Получить транзакции по счету", description = "Доступно владельцу счета или администратору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Транзакции найдены",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionDTO.class))),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @GetMapping("/account/{brokerAccountId}")
    @PreAuthorize("hasRole('ADMIN') or @clientSecurityService.isAccountOwner(#brokerAccountId, authentication)")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByBrokerAccountId(
            @Parameter(description = "ID брокерского счета", required = true)
            @PathVariable Long brokerAccountId) {
        return ResponseEntity.ok(transactionService.getTransactionsByBrokerAccountId(brokerAccountId));
    }

    /**
     * Создает новую транзакцию и обновляет баланс счета.
     *
     * @param transactionDTO данные новой транзакции
     * @return созданная транзакция
     */
    @Operation(summary = "Создать новую транзакцию", description = "Доступно владельцу счета или администратору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Транзакция создана",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TransactionDTO.class))),
            @ApiResponse(responseCode = "400", description = "Некорректные данные транзакции"),
            @ApiResponse(responseCode = "404", description = "Счет не найден"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or @clientSecurityService.isAccountOwner(#transactionDTO.brokerAccountId, authentication)")
    public ResponseEntity<TransactionDTO> createTransaction(
            @Parameter(description = "Данные транзакции", required = true)
            @Valid @RequestBody TransactionDTO transactionDTO) {
        return new ResponseEntity<>(transactionService.createTransaction(transactionDTO), HttpStatus.CREATED);
    }

    /**
     * Удаляет транзакцию (soft delete).
     *
     * @param id идентификатор транзакции
     */
    @Operation(summary = "Удалить транзакцию", description = "Доступно владельцу счета или администратору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Транзакция удалена"),
            @ApiResponse(responseCode = "404", description = "Транзакция не найдена"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @clientSecurityService.isTransactionOwner(#id, authentication)")
    public ResponseEntity<Void> deleteTransaction(
            @Parameter(description = "ID транзакции", required = true)
            @PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
}