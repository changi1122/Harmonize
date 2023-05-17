package com.example.harmonize.repository;

import com.example.harmonize.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(nativeQuery = true, value = "SELECT c.category_id from category c where c.category_name = :cname")
    Long findByCategory_name(@Param("cname")String cname);
}
