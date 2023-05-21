package com.example.harmonize.service;

import com.example.harmonize.dtos.MusicDTO;
import com.example.harmonize.entity.Music;
import com.example.harmonize.entity.UserVoice;
import com.example.harmonize.repository.*;
import com.example.harmonize.utility.Analyzer;
import com.example.harmonize.utility.Builder;
import jakarta.jws.Oneway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class MusicService {

    @Autowired
    private MusicRepository musicRepository;

    @Autowired
    private UserVoiceRepository userVoiceRepository;

    @Autowired
    private BookMarkRepository bookMarkRepository;

    private Builder builder = new Builder();
    private Analyzer  analyzer = new Analyzer();

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


    // order by category_id 추가 필요
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

    //Get music list where string = artist or music_name
    public List<Music> GetResultBySearch(String search){
        System.out.println(search);
        System.out.println(musicRepository.FindBySearch(search));
        return musicRepository.FindBySearch(search);
    }


    // Get music list where music_category_id = categroy_id
    public List<MusicDTO> GetListByCategory(Long uid, Long cid){
        List<Music> lists;
        if(cid==1){
            lists= musicRepository.FindAllByOrderByCategory_id();
        }else{
            lists = musicRepository.FindAllByCategory_id(cid);
        }
        UserVoice userVoice = userVoiceRepository.FindUserVoiceRange(uid);

        List<MusicDTO> musicDTOS = new ArrayList<>();

        for(Music list : lists){
            Double result = analyzer.GetPossibility(list, userVoice);
            
            Boolean bool =  (1 == bookMarkRepository.IsBookMarked(uid, list.getMusic_id()));

            if(result >= 70.0){
                musicDTOS.add(builder.BuildMicDTO(list, bool,(int)Math.round(result)));
            }
        }
        return musicDTOS;
    }

    //music Detail한 정보들 저장
    public void SetMusicDetails(Long mid, List<Double> list, Integer level){
        Music music = musicRepository.findById(mid).get();
        music.setMax(list.get(0));
        music.setMin(list.get(1));
        music.setLevel(level);

        musicRepository.save(music);
    }


}
