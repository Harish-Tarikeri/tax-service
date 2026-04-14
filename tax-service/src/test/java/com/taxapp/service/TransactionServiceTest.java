package com.taxapp.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.taxapp.entity.Transaction;
import com.taxapp.enums.ComplianceStatus;
import com.taxapp.enums.TransactionType;
import com.taxapp.enums.ValidationStatus;
import com.taxapp.repository.TransactionRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private RuleEngineService ruleEngineService;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // VALID TRANSACTION
    @Test
    void testValidTransaction() {

        Transaction txn = new Transaction();
        txn.setTransactionId("TXN1");
        txn.setDate(LocalDate.now());
        txn.setCustomerId("CUST1");
        txn.setAmount(BigDecimal.valueOf(1000));
        txn.setTaxRate(BigDecimal.valueOf(0.18));
        txn.setReportedTax(BigDecimal.valueOf(180));
        txn.setTransactionType(TransactionType.SALE);

        List<Transaction> result = transactionService.processTransactions(List.of(txn));

        assertEquals(ValidationStatus.SUCCESS, result.get(0).getValidationStatus());
        verify(transactionRepository, times(1)).save(txn);
    }

    // INVALID AMOUNT
    @Test
    void testInvalidAmount() {

        Transaction txn = new Transaction();
        txn.setTransactionId("TXN2");
        txn.setDate(LocalDate.now());
        txn.setCustomerId("CUST1");
        txn.setAmount(BigDecimal.ZERO);

        List<Transaction> result = transactionService.processTransactions(List.of(txn));

        assertEquals(ValidationStatus.FAILURE, result.get(0).getValidationStatus());
        assertEquals("Amount must be greater than 0", result.get(0).getFailureReason());
    }

    // UNDERPAID TAX
    @Test
    void testUnderpaidTax() {

        Transaction txn = new Transaction();
        txn.setTransactionId("TXN3");
        txn.setDate(LocalDate.now());
        txn.setCustomerId("CUST1");
        txn.setAmount(BigDecimal.valueOf(1000));
        txn.setTaxRate(BigDecimal.valueOf(0.18));
        txn.setReportedTax(BigDecimal.valueOf(150));
        txn.setTransactionType(TransactionType.SALE);

        List<Transaction> result = transactionService.processTransactions(List.of(txn));

        assertEquals(ComplianceStatus.UNDERPAID, result.get(0).getComplianceStatus());
    }

    // OVERPAID TAX
    @Test
    void testOverpaidTax() {

        Transaction txn = new Transaction();
        txn.setTransactionId("TXN4");
        txn.setDate(LocalDate.now());
        txn.setCustomerId("CUST1");
        txn.setAmount(BigDecimal.valueOf(1000));
        txn.setTaxRate(BigDecimal.valueOf(0.18));
        txn.setReportedTax(BigDecimal.valueOf(250));
        txn.setTransactionType(TransactionType.SALE);

        List<Transaction> result = transactionService.processTransactions(List.of(txn));

        assertEquals(ComplianceStatus.OVERPAID, result.get(0).getComplianceStatus());
    }

    // EXACT TAX MATCH
    @Test
    void testExactTaxMatch() {

        Transaction txn = new Transaction();
        txn.setTransactionId("TXN5");
        txn.setDate(LocalDate.now());
        txn.setCustomerId("CUST1");
        txn.setAmount(BigDecimal.valueOf(1000));
        txn.setTaxRate(BigDecimal.valueOf(0.18));
        txn.setReportedTax(BigDecimal.valueOf(180));
        txn.setTransactionType(TransactionType.SALE);

        List<Transaction> result = transactionService.processTransactions(List.of(txn));

        assertEquals(ComplianceStatus.COMPLIANT, result.get(0).getComplianceStatus());
    }

    // MISSING DATE
    @Test
    void testMissingDate() {

        Transaction txn = new Transaction();
        txn.setTransactionId("TXN6");
        txn.setCustomerId("CUST1");
        txn.setAmount(BigDecimal.valueOf(1000));

        List<Transaction> result = transactionService.processTransactions(List.of(txn));

        assertEquals(ValidationStatus.FAILURE, result.get(0).getValidationStatus());
        assertEquals("Date is required", result.get(0).getFailureReason());
    }

    // MULTIPLE TRANSACTIONS
    @Test
    void testMultipleTransactions() {

        Transaction txn1 = new Transaction();
        txn1.setTransactionId("TXN10");
        txn1.setDate(LocalDate.now());
        txn1.setCustomerId("CUST1");
        txn1.setAmount(BigDecimal.valueOf(1000));
        txn1.setTaxRate(BigDecimal.valueOf(0.18));
        txn1.setReportedTax(BigDecimal.valueOf(180));
        txn1.setTransactionType(TransactionType.SALE);

        Transaction txn2 = new Transaction();
        txn2.setTransactionId("TXN11");
        txn2.setDate(LocalDate.now());
        txn2.setCustomerId("CUST2");
        txn2.setAmount(BigDecimal.valueOf(2000));
        txn2.setTaxRate(BigDecimal.valueOf(0.18));
        txn2.setReportedTax(BigDecimal.valueOf(360));
        txn2.setTransactionType(TransactionType.SALE);

        List<Transaction> result =
                transactionService.processTransactions(List.of(txn1, txn2));

        assertEquals(2, result.size());
    }

    // RULE ENGINE CALLED
    @Test
    void testRuleEngineCalled() {

        Transaction txn = new Transaction();
        txn.setTransactionId("TXN12");
        txn.setDate(LocalDate.now());
        txn.setCustomerId("CUST1");
        txn.setAmount(BigDecimal.valueOf(15000));
        txn.setTaxRate(BigDecimal.valueOf(0.18));
        txn.setReportedTax(BigDecimal.valueOf(2700));
        txn.setTransactionType(TransactionType.SALE);

        transactionService.processTransactions(List.of(txn));

        verify(ruleEngineService, times(1)).applyRules(any());
    }

    // AUDIT SERVICE CALLED
    @Test
    void testAuditServiceCalled() {

        Transaction txn = new Transaction();
        txn.setTransactionId("TXN13");
        txn.setDate(LocalDate.now());
        txn.setCustomerId("CUST1");
        txn.setAmount(BigDecimal.valueOf(1000));
        txn.setTaxRate(BigDecimal.valueOf(0.18));
        txn.setReportedTax(BigDecimal.valueOf(180));
        txn.setTransactionType(TransactionType.SALE);

        transactionService.processTransactions(List.of(txn));

        verify(auditService, atLeastOnce()).log(any(), any(), any());
    }
}