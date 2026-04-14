package com.taxapp.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.taxapp.enums.ComplianceStatus;
import com.taxapp.enums.TransactionType;
import com.taxapp.enums.ValidationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "rules")
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ruleName;

    @Column(columnDefinition = "TEXT")
    private String configJson;

    private boolean active;
}