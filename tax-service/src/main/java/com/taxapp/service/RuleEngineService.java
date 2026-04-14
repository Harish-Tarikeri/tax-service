package com.taxapp.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.taxapp.entity.Rule;
import com.taxapp.entity.TaxException;
import com.taxapp.entity.Transaction;
import com.taxapp.enums.Severity;
import com.taxapp.repository.ExceptionRepository;
import com.taxapp.repository.RuleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RuleEngineService {

    private final RuleRepository ruleRepo;
    private final ExceptionRepository exceptionRepo;

    public void applyRules(Transaction t) {

        List<Rule> rules = ruleRepo.findByActiveTrue();

        for (Rule rule : rules) {

            // HIGH VALUE RULE
            if ("HIGH_VALUE".equals(rule.getRuleName())) {

                if (t.getAmount() != null &&
                        t.getAmount().compareTo(new BigDecimal("10000")) > 0) {

                    createException(t,
                            "HIGH_VALUE",
                            "High value transaction",
                            Severity.HIGH);
                }
            }

            // REFUND RULE
            if ("REFUND".equals(rule.getRuleName())) {

                if ("REFUND".equalsIgnoreCase(t.getTransactionType().name())) {

                    if (t.getAmount().compareTo(new BigDecimal("0")) <= 0) {
                        createException(t,
                                "REFUND",
                                "Invalid refund amount",
                                Severity.MEDIUM);
                    }
                }
            }
        }
    }

    private void createException(Transaction t,
                                 String ruleName,
                                 String message,
                                 Severity severity) {

        TaxException ex = new TaxException();

        ex.setTransactionId(t.getTransactionId());
        ex.setCustomerId(t.getCustomerId());
        ex.setRuleName(ruleName);
        ex.setMessage(message);
        ex.setSeverity(severity);
        ex.setTimestamp(LocalDateTime.now());

        exceptionRepo.save(ex);
    }
}