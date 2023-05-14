package com.example.harmonize.service;

import com.example.harmonize.entity.UserVoice;
import com.example.harmonize.repository.UserVoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserVoiceService {

    @Autowired
    private UserVoiceRepository userVoiceRepository;

    // save user detail
    public void SaveUserRange(Long uid, Double max, Double min){
        UserVoice userVoice = new UserVoice();
        userVoice.setUser_id(uid);
        userVoice.setFileName(String.valueOf(uid));
        userVoice.setMax(max);
        userVoice.setMin(min);

        userVoiceRepository.save(userVoice);
    }
}
