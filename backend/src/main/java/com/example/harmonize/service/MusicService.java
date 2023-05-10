package com.example.harmonize.service;

import com.example.harmonize.entity.Music;
import com.example.harmonize.repository.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class MusicService {

    @Autowired
    private MusicRepository musicRepository;

    public Long MusicSave(String title,String composer, String gender,
                          Integer TjNum, String link, String category, Long Convrt){

        Music music;
        if(Convrt == 0L){
            music = new Music();
        }else{
            music = musicRepository.findById(Convrt).get();
        }

        if(gender.equals("man")){
            music.setGender(0);
        }else{
            music.setGender(1);
        }
        music.setMusic_name(title);
        music.setArtist(composer);
        music.setTJ_Num(TjNum);
        music.setYoutube_link(link);
        music.setCategory_id(Long.parseLong(category));

        return musicRepository.save(music).getMusic_id();
    }

    public List<Music> getAllMusic(){
        return musicRepository.findAll();
    }

    public Music FindByID(String id){
        return musicRepository.findById(Long.parseLong(id)).get();
    }

    public void DeleteByID(String id){
        musicRepository.deleteById(Long.parseLong(id));
    }

    public void SaveFile(String fileName, Long id, Integer type){
        Music music = musicRepository.findById(id).get();
        if(type==0){
            music.setImg_link(fileName);
        }else{
            music.setFilename(fileName);
        }
        musicRepository.save(music);
    }

}
