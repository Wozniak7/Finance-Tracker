package com.portfolio.finance.repository;

import com.portfolio.finance.domain.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCategoryId(Long categoryId);

    List<Transaction> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
