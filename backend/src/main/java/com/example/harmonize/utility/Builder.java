package com.example.harmonize.utility;

import com.example.harmonize.dtos.MusicDTO;
import com.example.harmonize.dtos.MusicDetailDTO;
import com.example.harmonize.entity.Music;

import java.util.ArrayList;
import java.util.List;

public class Builder {

    public MusicDTO BuildMicDTO(Music list, Boolean bool, Integer result){
        MusicDTO musicDTO = MusicDTO.builder()
                .music_id(list.getMusic_id())
                .music_name(list.getMusic_name())
                .artist(list.getArtist())
                .img_link(list.getImg_link())
                .level(list.getLevel())
                .range_avg(result)
                .is_prefer(bool)
                .category_id(list.getCategory_id())
                .build();

        return musicDTO;
    }

    public MusicDetailDTO BuildDetailDTO(Music music, Boolean isPrefer, Integer matchRate) {
        MusicDetailDTO musicDetailDTO = MusicDetailDTO.builder()
                .music_id(music.getMusic_id())
                .music_name(music.getMusic_name())
                .artist(music.getArtist())
                .img_link(music.getImg_link())
                .level(music.getLevel())
                .range_avg(matchRate)
                .is_prefer(isPrefer)
                .category_id(music.getCategory_id())
                .max(music.getMax())
                .min(music.getMin())
                .tj_num(music.getTJ_Num())
                .build();

        return musicDetailDTO;
    }

}
