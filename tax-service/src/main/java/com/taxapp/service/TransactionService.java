package com.taxapp.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.taxapp.dto.CustomerReportDTO;
import com.taxapp.entity.Transaction;
import com.taxapp.enums.ComplianceStatus;
import com.taxapp.enums.ValidationStatus;
import com.taxapp.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final RuleEngineService ruleEngineService;
    private final AuditService auditService;

    
    public List<Transaction> processTransactions(List<Transaction> transactions) {

        List<Transaction> processedList = new ArrayList<>();

        for (Transaction txn : transactions) {

            try {
                // 1. AUDIT - INGESTION
                auditService.log("INGESTION", txn.getTransactionId(),
                        "Received transaction at " + LocalDateTime.now());

                // 2. VALIDATION
                validateTransaction(txn);

                if (txn.getValidationStatus() == ValidationStatus.FAILURE) {
                    transactionRepository.save(txn);
                    processedList.add(txn);
                    continue;
                }

                // 3. TAX CALCULATION
                calculateTax(txn);

                // 4. RULE ENGINE
                ruleEngineService.applyRules(txn);

                // 5. AUDIT - TAX COMPUTATION
                auditService.log("TAX_COMPUTATION", txn.getTransactionId(),
                        "ExpectedTax=" + txn.getExpectedTax() +
                                ", TaxGap=" + txn.getTaxGap() +
                                ", Status=" + txn.getComplianceStatus());

            } catch (Exception ex) {

                txn.setValidationStatus(ValidationStatus.FAILURE);
                txn.setFailureReason(ex.getMessage());

                // AUDIT FAILURE
                auditService.log("INGESTION", txn.getTransactionId(),
                        "Validation failed: " + ex.getMessage());
            }

            // Save transaction (both success & failure)
            transactionRepository.save(txn);

            processedList.add(txn);
        }

        return processedList;
    }

    
    private void validateTransaction(Transaction txn) {

        // Transaction ID
        if (txn.getTransactionId() == null || txn.getTransactionId().isEmpty()) {
            txn.setValidationStatus(ValidationStatus.FAILURE);
            txn.setFailureReason("TransactionId is required");
            return;
        }

        // Date
        if (txn.getDate() == null) {
            txn.setValidationStatus(ValidationStatus.FAILURE);
            txn.setFailureReason("Date is required");
            return;
        }

        // Customer ID
        if (txn.getCustomerId() == null || txn.getCustomerId().isEmpty()) {
            txn.setValidationStatus(ValidationStatus.FAILURE);
            txn.setFailureReason("CustomerId is required");
            return;
        }

        // Amount
        if (txn.getAmount() == null || txn.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            txn.setValidationStatus(ValidationStatus.FAILURE);
            txn.setFailureReason("Amount must be greater than 0");
            return;
        }

        // Tax Rate
        if (txn.getTaxRate() == null || txn.getTaxRate().compareTo(BigDecimal.ZERO) < 0) {
            txn.setValidationStatus(ValidationStatus.FAILURE);
            txn.setFailureReason("Invalid tax rate");
            return;
        }

        // Transaction Type
        if (txn.getTransactionType() == null) {
            txn.setValidationStatus(ValidationStatus.FAILURE);
            txn.setFailureReason("Transaction type is required");
            return;
        }

        //  If all validations pass
        txn.setValidationStatus(ValidationStatus.SUCCESS);
        txn.setFailureReason(null);
    }

    
    private void calculateTax(Transaction txn) {

        BigDecimal amount = txn.getAmount();
        BigDecimal taxRate = txn.getTaxRate();

        // expectedTax = amount * taxRate
        BigDecimal expectedTax = amount.multiply(taxRate);
        txn.setExpectedTax(expectedTax);

        // If reportedTax missing
        if (txn.getReportedTax() == null) {
            txn.setComplianceStatus(ComplianceStatus.NON_COMPLIANT);
            txn.setTaxGap(null);
            return;
        }

        // taxGap = expected - reported
        BigDecimal taxGap = expectedTax.subtract(txn.getReportedTax());
        txn.setTaxGap(taxGap);

        // Compliance logic
        if (taxGap.abs().compareTo(BigDecimal.ONE) <= 0) {
            txn.setComplianceStatus(ComplianceStatus.COMPLIANT);
        } else if (taxGap.compareTo(BigDecimal.ONE) > 0) {
            txn.setComplianceStatus(ComplianceStatus.UNDERPAID);
        } else {
            txn.setComplianceStatus(ComplianceStatus.OVERPAID);
        }
    }

    
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    
    public List<Transaction> getByCustomerId(String customerId) {
        return transactionRepository.findByCustomerId(customerId);
    }

    
    public void deleteTransaction(String transactionId) {
        transactionRepository.deleteById(transactionId);
    }
    
    public List<CustomerReportDTO> getCustomerReport() {
        return transactionRepository.getCustomerReport();
    }
}