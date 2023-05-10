package com.example.harmonize.repository;

import com.example.harmonize.entity.Prefer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferRepository extends JpaRepository<Prefer, Long> {
}
