package com.example.harmonize.service;

import com.example.harmonize.entity.UserVoice;
import com.example.harmonize.repository.UserVoiceRepository;
import com.example.harmonize.utility.Analyzer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UserVoiceService {

    @Autowired
    private UserVoiceRepository userVoiceRepository;

    private Analyzer analyzer = new Analyzer();

    // save user detail
    public void SaveUserRange(Long uid, Double max, Double min) throws IOException {
        analyzer.FindMusicRange();
        UserVoice userVoice = new UserVoice();
        userVoice.setUser_id(uid);
        userVoice.setFileName(String.valueOf(uid));
        userVoice.setMax(max);
        userVoice.setMin(min);

        userVoiceRepository.save(userVoice);
    }
}
