package com.example.harmonize.service;

import com.example.harmonize.repository.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MusicService {

    @Autowired
    private MusicRepository musicRepository;


}
