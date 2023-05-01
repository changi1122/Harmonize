package com.example.harmonize.controller;

import com.example.harmonize.entity.Music;
import com.example.harmonize.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PageController {

    @Autowired
    private MusicService musicService;

    @GetMapping(value = "/music/show")
    public String ShowMusicList(Model model){

        List<Music> musicList = musicService.getAllMusic();

        System.out.println(musicList);
        model.addAttribute("musicList", musicList);
        return "musicList";
    }
}
