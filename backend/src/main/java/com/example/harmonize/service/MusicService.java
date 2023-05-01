package com.example.harmonize.service;

import com.example.harmonize.entity.Music;
import com.example.harmonize.repository.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MusicService {

    @Autowired
    private MusicRepository musicRepository;

    public Long MusicSave(String title,String composer, String gender, Integer time,
                          Integer TjNum, String link, String category, String img_filename){

        Music music = new Music();
        music.setMusic_name(title);
        music.setArtist(composer);
        music.setGender(gender=="man"?0:1);
        music.setTime(time);
        music.setMusic_numberTJ(TjNum);
        music.setYoutube_link(link);
        music.setCategory(category);
        music.setImage_link(img_filename);

        return musicRepository.save(music).getMusic_id();
    }

}
