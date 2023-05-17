package com.example.harmonize.repository;

import com.example.harmonize.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findById(int id);

    boolean existsByUsername(String username);

    @Query(nativeQuery = true, value = "SELECT * FROM user u WHERE u.id = :id ;")
    User SendUserInfoC(@Param("id")Long id);
}
