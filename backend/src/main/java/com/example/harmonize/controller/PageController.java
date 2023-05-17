package com.example.harmonize.controller;

import com.example.harmonize.entity.Music;
import com.example.harmonize.service.MusicService;
import com.example.harmonize.utility.Files;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PageController {

    @Autowired
    private MusicService musicService;

    Files files = new Files();

    //admin page, show all music table info, Checked 23.05.14
    @GetMapping(value = "/music/show")
    public String ShowMusicList(Model model){

        List<Music> musicList = musicService.getAllMusic();

        System.out.println(musicList);
        model.addAttribute("musicList", musicList);
        return "musicList";
    }

    //admin page, update music, Checked 23.05.14
    @GetMapping("/music/update/{id}")
    public String FindMusicByID(@PathVariable String id, Model model){
        System.out.println("music Update");
        System.out.println(id);
        System.out.println(musicService.FindByID(id));
        model.addAttribute("music", musicService.FindByID(id));

        return "refactor";
    }

    //admin page, delete music from Music by id, Checked 23.05.14
    @PostMapping("/music/delete")
    public String DeleteMusicByID(@RequestParam("id") String id){
        musicService.DeleteByID(id);
        return "musicList";
    }

    //admin page, update music from Music by id, Checked 23.05.13
    @PostMapping("/music/update")
    public String UpdateMusic(@RequestParam("id") Long id, @RequestParam("title") String title, @RequestParam("composer") String composer,
                              @RequestParam("gender") String gender, @RequestParam("TjNum") Integer TjNum,
                              @RequestParam("link") String link, @RequestParam("category") String category,
                              @RequestParam("file") MultipartFile file) throws IOException {

        musicService.MusicSave(title, composer, gender, TjNum, link, category, id);
        System.out.println(id);

        String fileName = files.SaveFile(file, id, 0);
        musicService.SaveFile(fileName, id, 0);

        return "/";

    }
}
