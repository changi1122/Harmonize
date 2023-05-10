package com.example.harmonize.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "music")
public class Music {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "music_id")
    private Long music_id;

    private String music_name;
    private String artist;
    private Integer gender;
    private String img_link;
    private String filename;
    private Long category_id;
    private Integer TJ_Num;
    private String youtube_link;
    private Double max;
    private Double min;
    private Integer level;
}
