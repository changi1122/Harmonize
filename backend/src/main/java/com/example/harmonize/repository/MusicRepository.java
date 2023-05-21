package com.example.harmonize.repository;

import com.example.harmonize.dtos.MusicDTO;
import com.example.harmonize.entity.Music;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusicRepository extends JpaRepository<Music, Long> {

    // search music
    @Query(nativeQuery = true, value = "SELECT * from music m where m.music_name like %:search% OR m.artist like %:search% ORDER BY m.music_id")
    List<Music> FindBySearch(@Param("search")String search);

    @Query(nativeQuery = true, value = "SELECT  * from music ")
    List<Music> FindAllByOrderByCategory_id();

    @Query(nativeQuery = true, value = "SELECT * from music m where m.category_id = :cid")
    List<Music> FindAllByCategory_id(@Param("cid") Long cid);
}
