package com.taxapp.service;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.taxapp.entity.Rule;
import com.taxapp.entity.Transaction;
import com.taxapp.enums.TransactionType;
import com.taxapp.repository.ExceptionRepository;
import com.taxapp.repository.RuleRepository;

class RuleEngineServiceTest {

    @Mock
    private RuleRepository ruleRepository;

    @Mock
    private ExceptionRepository exceptionRepository;

    @InjectMocks
    private RuleEngineService ruleEngineService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // HIGH VALUE RULE TRIGGER
    @Test
    void testHighValueRule() {

        Transaction txn = new Transaction();
        txn.setTransactionId("TXN100");
        txn.setCustomerId("CUST1");
        txn.setAmount(BigDecimal.valueOf(20000));

        Rule rule = new Rule();
        rule.setRuleName("HIGH_VALUE");
        rule.setActive(true);

        when(ruleRepository.findByActiveTrue()).thenReturn(List.of(rule));

        ruleEngineService.applyRules(txn);

        verify(exceptionRepository, times(1)).save(any());
    }

    // NO RULE TRIGGERED
    @Test
    void testNoRuleTriggered() {

        Transaction txn = new Transaction();
        txn.setTransactionId("TXN101");
        txn.setAmount(BigDecimal.valueOf(500));

        Rule rule = new Rule();
        rule.setRuleName("HIGH_VALUE");
        rule.setActive(true);

        when(ruleRepository.findByActiveTrue()).thenReturn(List.of(rule));

        ruleEngineService.applyRules(txn);

        verify(exceptionRepository, never()).save(any());
    }

    // HIGH VALUE NOT TRIGGERED (boundary)
    @Test
    void testHighValueNotTriggered() {

        Transaction txn = new Transaction();
        txn.setTransactionId("TXN102");
        txn.setCustomerId("CUST1");
        txn.setAmount(BigDecimal.valueOf(10000)); // boundary

        Rule rule = new Rule();
        rule.setRuleName("HIGH_VALUE");
        rule.setActive(true);

        when(ruleRepository.findByActiveTrue()).thenReturn(List.of(rule));

        ruleEngineService.applyRules(txn);

        verify(exceptionRepository, never()).save(any());
    }

    // REFUND INVALID AMOUNT
    @Test
    void testRefundInvalidAmount() {

        Transaction txn = new Transaction();
        txn.setTransactionId("TXN103");
        txn.setCustomerId("CUST1");
        txn.setAmount(BigDecimal.valueOf(0)); // invalid
        txn.setTransactionType(TransactionType.REFUND);

        Rule rule = new Rule();
        rule.setRuleName("REFUND");
        rule.setActive(true);

        when(ruleRepository.findByActiveTrue()).thenReturn(List.of(rule));

        ruleEngineService.applyRules(txn);

        verify(exceptionRepository, times(1)).save(any());
    }

    // REFUND VALID CASE
    @Test
    void testRefundValid() {

        Transaction txn = new Transaction();
        txn.setTransactionId("TXN104");
        txn.setCustomerId("CUST1");
        txn.setAmount(BigDecimal.valueOf(500));
        txn.setTransactionType(TransactionType.REFUND);

        Rule rule = new Rule();
        rule.setRuleName("REFUND");
        rule.setActive(true);

        when(ruleRepository.findByActiveTrue()).thenReturn(List.of(rule));

        ruleEngineService.applyRules(txn);

        verify(exceptionRepository, never()).save(any());
    }
}