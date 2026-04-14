package com.taxapp.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taxapp.entity.AuditLog;
import com.taxapp.repository.AuditRepository;

@Service
public class AuditService {

    @Autowired
    private AuditRepository repo;

    public void log(String event, String txnId, String details) {

        AuditLog log = new AuditLog();
        log.setEventType(event);
        log.setTransactionId(txnId);
        log.setTimestamp(LocalDateTime.now());
        log.setDetailJson(details);

        repo.save(log);
    }
}
