package com.taxapp.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.taxapp.enums.ComplianceStatus;
import com.taxapp.enums.Severity;
import com.taxapp.enums.TransactionType;
import com.taxapp.enums.ValidationStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "exceptions")
public class TaxException {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId;
    private String customerId;
    private String ruleName;

    @Enumerated(EnumType.STRING)
    private Severity severity;

    private String message;
    private LocalDateTime timestamp;
}