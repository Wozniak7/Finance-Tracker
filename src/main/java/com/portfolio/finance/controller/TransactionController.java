package com.portfolio.finance.controller;

import com.portfolio.finance.domain.dto.TransactionDTO;
import com.portfolio.finance.domain.dto.TransactionSummaryDTO;
import com.portfolio.finance.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/summary")
    public ResponseEntity<TransactionSummaryDTO> getSummary(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        
        int targetYear = (year != null) ? year : LocalDate.now().getYear();
        int targetMonth = (month != null) ? month : LocalDate.now().getMonthValue();
        
        return ResponseEntity.ok(transactionService.getSummaryByMonth(targetYear, targetMonth));
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> create(@RequestBody @Valid TransactionDTO dto) {
        TransactionDTO created = transactionService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
