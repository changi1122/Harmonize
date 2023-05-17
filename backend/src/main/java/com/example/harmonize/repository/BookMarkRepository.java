package com.example.harmonize.repository;

import com.example.harmonize.entity.BookMark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.naming.Name;
import java.util.List;

@Repository
public interface BookMarkRepository extends JpaRepository<BookMark, Long> {

    //user_id, music_id 컬럼이 존재하면 Ture, 아니면 False 반환
    @Query(nativeQuery = true, value = "SELECT EXISTS(select 1 from bookmark bk where bk.user_id = :uid AND bk.music_id =:mid ) as cnt")
    Integer IsBookMarked( @Param("uid") Long uid, @Param("mid") Long mid);

    // user_id가 bookmark한 모든 music_id를 List<Long>으로 반환
    @Query(nativeQuery = true, value = "SELECT bk.music_id from bookmark bk where bk.user_id = :uid ORDER BY bk.bookmark_id DESC ")
    List<Long> findAllByUser_id(@Param("uid") Long uid);

    @Query(nativeQuery = true, value = "Select bk.bookmark_id from bookmark bk where bk.user_id=:uid AND bk.music_id = :mid")
    Long findBookMarkID(@Param("uid") Long uid, @Param("mid") Long mid);


}
