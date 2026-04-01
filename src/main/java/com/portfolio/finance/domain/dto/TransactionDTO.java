package com.portfolio.finance.domain.dto;

import com.portfolio.finance.domain.enums.TransactionType;
import com.portfolio.finance.domain.model.Transaction;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private Long id;

    @NotNull(message = "Description is mandatory")
    private String description;

    @NotNull(message = "Amount is mandatory")
    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Date is mandatory")
    private LocalDate date;

    @NotNull(message = "Transaction type is mandatory")
    private TransactionType type;

    @NotNull(message = "Category ID is mandatory")
    private Long categoryId;

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.description = transaction.getDescription();
        this.amount = transaction.getAmount();
        this.date = transaction.getDate();
        this.type = transaction.getType();
        this.categoryId = transaction.getCategory() != null ? transaction.getCategory().getId() : null;
    }
}
