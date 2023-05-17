package com.example.harmonize.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "prefer")
public class Prefer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prefer_id")
    private Long prefer_id;

    private Long user_id;

    private Long category_id;
}
