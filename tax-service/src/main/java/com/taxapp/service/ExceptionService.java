package com.taxapp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.taxapp.entity.TaxException;
import com.taxapp.enums.Severity;
import com.taxapp.repository.ExceptionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExceptionService {

    private final ExceptionRepository exceptionRepo;

    public List<TaxException> getAllExceptions() {
        return exceptionRepo.findAll();
    }

    public List<TaxException> filterExceptions(String customerId,
                                               Severity severity,
                                               String ruleName) {

        List<TaxException> list = exceptionRepo.findAll();

        return list.stream()
                .filter(e -> customerId == null || e.getCustomerId().equals(customerId))
                .filter(e -> severity == null || e.getSeverity() == severity)
                .filter(e -> ruleName == null || e.getRuleName().equalsIgnoreCase(ruleName))
                .collect(Collectors.toList());
    }
}