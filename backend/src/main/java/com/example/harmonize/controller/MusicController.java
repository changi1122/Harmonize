package com.example.harmonize.controller;

import com.example.harmonize.entity.Music;
import com.example.harmonize.service.MusicService;
import com.example.harmonize.utility.Connector;
import com.example.harmonize.utility.Files;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


@RestController
public class MusicController {


    Connector connector = new Connector();
    Files files = new Files();

    @Autowired
    private MusicService musicService;

    @PostMapping("/music/save")
    public String SaveMusic(@RequestParam("title") String title, @RequestParam("composer") String composer,
                          @RequestParam("gender") String gender, @RequestParam("TjNum") Integer TjNum,
                            @RequestParam("link") String link, @RequestParam("category") String category,
                            @RequestParam("split") String split, @RequestParam("file") MultipartFile file, @RequestParam("music_file")MultipartFile MF) throws IOException {

        Long id =  musicService.MusicSave(title, composer, gender, TjNum, link, category, 0L);
        System.out.println(id);

        String ImgfileName = files.SaveFile(file, id, 0);
        System.out.println(ImgfileName);
        musicService.SaveFile(ImgfileName, id, 0);
        // img 파일 저장

        String MusicfileName = files.SaveFile(MF, id, 1);
        System.out.println(MusicfileName);
        // music 파일 저장

        connector.SocketCall(id+".m4a",  id, Long.parseLong(split));

        return "/";
        // Need to make Music Table
    }

    @GetMapping("/music/test")
    public void testMusic(){
        String string;
        System.out.println("sss");
        string = connector.SocketCall("1024test5-2.m4a", 29L, 0L);
        System.out.println(string);
    }
    
    @GetMapping("/music/help")
    public String test(){
        return "hello";
    }

}
