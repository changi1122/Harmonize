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

    @Query(nativeQuery = true, value = "SELECT * from prefer p where p.user_id = :uid")
    List<Prefer> findAllByUser_id(@Param("uid")Long uid);

    @Query(nativeQuery = true, value = "select c.category_name from prefer p right join category c on p.category_id = c.category_id where p.user_id = :uid")
    List<String> findCategoryNameByUser_id(@Param("uid")Long uid);

    @Query(nativeQuery = true, value = "select p.prefer_id from prefer p where p.user_id = :uid AND p.category_id =:cid")
    Long findByCategory_idAndUser_id(@Param("uid")Long uid, @Param("cid")Long cid);

    @Query(nativeQuery = true, value = "delete from prefer where user_id = :uid")
    void deleteAllByUser_id(@Param("uid")Long uid);
}
