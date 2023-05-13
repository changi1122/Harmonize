package com.example.harmonize.repository;

import com.example.harmonize.entity.Prefer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


// 사용자가 선호하는 카테고리 저장하는 Table 
@Repository
public interface PreferRepository extends JpaRepository<Prefer, Long> {
}
