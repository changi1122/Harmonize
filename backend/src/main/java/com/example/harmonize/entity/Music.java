package com.example.harmonize.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "music")
public class Music {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "music_id")
    private Long music_id;

    private String music_name;
    private String artist;
    private Integer gender;
    private Integer tiem;
    private String image_link;
    private Integer music_numberTJ;
    private String youtube_link;
    private String category;
    private String filename;
    private Double max;
    private Double min;
    private Integer level;
}
