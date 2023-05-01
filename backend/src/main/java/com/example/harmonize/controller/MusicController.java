package com.example.harmonize.controller;

import com.example.harmonize.entity.Music;
import com.example.harmonize.service.MusicService;
import com.example.harmonize.utility.Connector;
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

    @Autowired
    private MusicService musicService;

    @PostMapping("/music/save")
    public String SaveMusic(@RequestParam("title") String title, @RequestParam("composer") String composer,
                          @RequestParam("gender") String gender, @RequestParam("time") Integer time,
                          @RequestParam("TjNum") Integer TjNum, @RequestParam("link") String link,
                          @RequestParam("category") String category, @RequestParam("file") MultipartFile file) throws IOException {

        String originalfileName = file.getOriginalFilename();
        String fullPath = "D:/AppP/Harmonize/backend/src/main/resources/img/" + originalfileName;
        file.transferTo(new File(fullPath));
        System.out.println(title + " " + composer +  " " + gender + " " + time + " " + originalfileName + " " + TjNum + " " + link + " " + category);



        Long id =  musicService.MusicSave(title, composer, gender, time, TjNum, link, category, originalfileName);
        System.out.println(id);
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
