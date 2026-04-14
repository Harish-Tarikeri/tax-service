package com.taxapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taxapp.entity.AuditLog;

public interface AuditRepository extends JpaRepository<AuditLog, Long> {
	
}