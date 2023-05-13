package com.example.harmonize.repository;

import com.example.harmonize.entity.UserVoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVoiceRepository extends JpaRepository<UserVoice, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM uservoice uv WHERE uv.user_id=:uid")
    UserVoice FindUserVoiceRange(@Param("uid")Long uid);
}
