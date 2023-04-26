package com.example.harmonize.controller;

import com.example.harmonize.entity.Music;
import com.example.harmonize.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MusicController {

    @Autowired
    private MusicService musicService;

    @PostMapping("/music/save")
    public void SaveMusic(@RequestBody Music music, Model model){
        // Need to make Music Table

    }


}
