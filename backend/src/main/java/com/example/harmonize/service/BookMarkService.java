package com.example.harmonize.service;

import com.example.harmonize.dtos.MusicDTO;
import com.example.harmonize.entity.BookMark;
import com.example.harmonize.entity.Music;
import com.example.harmonize.entity.UserVoice;
import com.example.harmonize.repository.BookMarkRepository;
import com.example.harmonize.repository.MusicRepository;
import com.example.harmonize.repository.UserRepository;
import com.example.harmonize.repository.UserVoiceRepository;
import com.example.harmonize.utility.Analyzer;
import com.example.harmonize.utility.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookMarkService {

    @Autowired
    private BookMarkRepository bookMarkRepository;

    @Autowired
    private UserVoiceRepository userVoiceRepository;

    @Autowired
    private MusicRepository musicRepository;

    private Analyzer analyzer = new Analyzer();
    private Builder builder = new Builder();


    //북마크 저장
    public void SaveBookMark(Long uid, Long mid){
        BookMark bookMark = new BookMark();
        bookMark.setMusic_id(mid);
        bookMark.setUser_id(uid);
        bookMarkRepository.save(bookMark);
    }

    // user Table id 기준으로 일치하는 컬럼 list로 제공
    public List<MusicDTO> GetMusicListByUID(Long uid){
        List<Long> musics = bookMarkRepository.findAllByUser_id(uid);
        UserVoice userVoice = userVoiceRepository.FindUserVoiceRange(uid);

        List<MusicDTO> musicDTOS = new ArrayList<>();

        for( Long mid : musics){
            Music music = musicRepository.findById(mid).get();
            Double result = analyzer.GetPossibility(music, userVoice);
            musicDTOS.add(builder.BuildMicDTO(music, true,(int)Math.round(result)));
        }

        return musicDTOS;
    }
}
