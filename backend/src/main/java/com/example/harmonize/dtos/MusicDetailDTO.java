package com.example.harmonize.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MusicDetailDTO implements Serializable  {
    private Long music_id;
    private String music_name;
    private String artist;
    private String img_link;
    private Integer level;
    private Integer range_avg;
    private Boolean is_prefer;
    private Long category_id;
    private Double max;
    private Double min;
    private Integer tj_num;
    private Double high;
    private Double low;
}
