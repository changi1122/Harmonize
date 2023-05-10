package com.example.harmonize.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "uservoice")
public class UserVoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_vid")
    private Long user_vid;

    private Long user_id;

    private String fileName;

    private Double max;

    private Double min;
}
