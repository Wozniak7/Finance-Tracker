package com.portfolio.finance.service;

import com.portfolio.finance.domain.dto.TransactionDTO;
import com.portfolio.finance.domain.dto.TransactionSummaryDTO;
import com.portfolio.finance.domain.enums.TransactionType;
import com.portfolio.finance.domain.model.Category;
import com.portfolio.finance.domain.model.Transaction;
import com.portfolio.finance.repository.CategoryRepository;
import com.portfolio.finance.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public TransactionSummaryDTO getSummaryByMonth(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        List<Transaction> transactions = transactionRepository.findByDateBetween(startDate, endDate);

        BigDecimal totalIncome = transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balance = totalIncome.subtract(totalExpense);

        List<TransactionDTO> dtos = transactions.stream()
                .map(TransactionDTO::new)
                .collect(Collectors.toList());

        return TransactionSummaryDTO.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .balance(balance)
                .transactions(dtos)
                .build();
    }

    @Transactional
    public TransactionDTO create(TransactionDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + dto.getCategoryId()));

        Transaction transaction = Transaction.builder()
                .description(dto.getDescription())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .type(dto.getType())
                .category(category)
                .build();

        transaction = transactionRepository.save(transaction);
        return new TransactionDTO(transaction);
    }

    @Transactional
    public void delete(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new IllegalArgumentException("Transaction not found with id: " + id);
        }
        transactionRepository.deleteById(id);
    }
}
