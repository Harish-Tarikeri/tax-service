package com.taxapp.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taxapp.entity.TaxException;
import com.taxapp.entity.Transaction;
import com.taxapp.enums.Severity;
import com.taxapp.service.ExceptionService;
import com.taxapp.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final ExceptionService exceptionService;

   
    // Upload Transactions
    
    @PostMapping("/transactions/upload")
    public ResponseEntity<List<Transaction>> uploadTransactions(
            @RequestBody List<Transaction> transactions) {

        List<Transaction> result = transactionService.processTransactions(transactions);
        return ResponseEntity.ok(result);
    }

    
    // Get All Transactions
    
    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    
    // Get Transactions by Customer
    
    @GetMapping("/transactions/customer/{customerId}")
    public ResponseEntity<List<Transaction>> getByCustomer(
            @PathVariable String customerId) {

        return ResponseEntity.ok(transactionService.getByCustomerId(customerId));
    }

    
    // Delete Transaction
    
    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable String id) {

        transactionService.deleteTransaction(id);
        return ResponseEntity.ok("Transaction deleted successfully");
    }

    
    // Get All Exceptions
    
    @GetMapping("/exceptions")
    public ResponseEntity<List<TaxException>> getAllExceptions() {
        return ResponseEntity.ok(exceptionService.getAllExceptions());
    }

    
    // Filter Exceptions
    
    @GetMapping("/exceptions/filter")
    public ResponseEntity<List<TaxException>> filterExceptions(
            @RequestParam(required = false) String customerId,
            @RequestParam(required = false) Severity severity,
            @RequestParam(required = false) String ruleName) {

        return ResponseEntity.ok(
                exceptionService.filterExceptions(customerId, severity, ruleName)
        );
    }
    
    // Reporting Api
    
    @GetMapping("/report/customer")
    public ResponseEntity<?> getCustomerReport() {
        return ResponseEntity.ok(transactionService.getCustomerReport());
    }
}
