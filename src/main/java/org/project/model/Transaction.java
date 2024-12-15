package org.project.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Transaction {

    private Long id;

    private Long brokerAccountId;

    private LocalDateTime transactionDate;

    private Double amount;

    private String type;
}
