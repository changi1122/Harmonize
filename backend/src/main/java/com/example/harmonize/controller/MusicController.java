package com.example.harmonize.controller;

import com.example.harmonize.entity.Music;
import com.example.harmonize.service.MusicService;
import com.example.harmonize.utility.Connector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(value = "/api")
public class MusicController {


    Connector connector = new Connector();

    @Autowired
    private MusicService musicService;

    @GetMapping("/music/save")
    public void SaveMusic(){
        // Need to make Music Table
        System.out.println("hello");

    }


    @GetMapping("/music/test")
    public void testMusic(){
        String string;
        System.out.println("sss");
        string = connector.SocketCall("1024test5-2.m4a", 29L, 0L);
        System.out.println(string);
    }

}
