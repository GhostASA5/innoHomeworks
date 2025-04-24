package org.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BrokerAccountDTO {

    private Long id;

    @NotNull(message = "Client ID is mandatory")
    private Long clientId;

    @NotBlank(message = "Account number is mandatory")
    private String accountNumber;

    @NotNull(message = "Balance is mandatory")
    @PositiveOrZero(message = "Balance cannot be negative")
    private Double balance;
}
