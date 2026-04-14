package com.taxapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.taxapp.entity.Rule;

public interface RuleRepository extends JpaRepository<Rule, Long> {
    List<Rule> findByActiveTrue();
}