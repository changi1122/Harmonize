package com.example.harmonize.repository;

import com.example.harmonize.entity.UserVoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVoiceRepository extends JpaRepository<UserVoice, Long> {
}
