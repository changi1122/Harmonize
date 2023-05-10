package com.example.harmonize.service;

import com.example.harmonize.repository.UserVoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserVoiceService {

    @Autowired
    private UserVoiceRepository userVoiceRepository;
}
