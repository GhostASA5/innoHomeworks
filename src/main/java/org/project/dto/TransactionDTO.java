package org.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {

    private Long id;

    @NotNull(message = "Broker account ID is mandatory")
    private Long brokerAccountId;

    @PastOrPresent(message = "Transaction date cannot be in the future")
    private LocalDateTime transactionDate;

    @NotNull(message = "Amount is mandatory")
    private Double amount;

    @NotBlank(message = "Type is mandatory")
    private String type;
}