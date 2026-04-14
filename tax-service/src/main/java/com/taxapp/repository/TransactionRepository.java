package com.taxapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.taxapp.dto.CustomerReportDTO;
import com.taxapp.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    List<Transaction> findByCustomerId(String customerId);

   
    @Query("""
    SELECT new com.taxapp.dto.CustomerReportDTO(
        t.customerId,
        SUM(t.amount),
        SUM(t.expectedTax),
        SUM(t.reportedTax),
        SUM(t.taxGap),
        100.0 - (SUM(CASE WHEN t.complianceStatus <> 'COMPLIANT' THEN 1 ELSE 0 END) * 100.0 / COUNT(t))
    )
    FROM Transaction t
    GROUP BY t.customerId
    """)
    List<CustomerReportDTO> getCustomerReport();
}
