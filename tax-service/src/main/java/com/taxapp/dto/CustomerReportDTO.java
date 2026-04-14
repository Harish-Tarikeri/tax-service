package com.taxapp.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerReportDTO {

    private String customerId;
    private BigDecimal totalAmount;
    private BigDecimal totalExpectedTax;
    private BigDecimal totalReportedTax;
    private BigDecimal totalTaxGap;
    private Double complianceScore;
}
