package com.example.harmonize.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "bookmark")
public class BookMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Long bookmark_id;

    private Long user_id;

    private Long music_id;
}
