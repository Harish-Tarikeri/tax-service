package com.taxapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taxapp.entity.TaxException;

public interface ExceptionRepository extends JpaRepository<TaxException, Long> {
	
}
