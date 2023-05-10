package com.example.harmonize.repository;

import com.example.harmonize.entity.Attempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttemptRepository extends JpaRepository<Attempt, Long> {
    Optional<Attempt> findAttemptByUserId(Long userId);
}