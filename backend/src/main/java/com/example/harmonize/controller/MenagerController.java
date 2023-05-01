package com.example.harmonize.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@RestController
public class MenagerController {

    @PostMapping("/test/data")
    public String TestData(@RequestParam("title") String title, @RequestParam("composer") String composer, @RequestParam("gender") String gender, @RequestParam("time") Integer time, @RequestParam("TjNum") Integer TjNum, @RequestParam("link") String link, @RequestParam("category") String category, @RequestParam("MkDate") String MkDate, @RequestParam("file") MultipartFile file) throws IOException {
        String originalfileName = file.getOriginalFilename();
        String fullPath = "D:/AppP/Harmonize/backend/src/main/resources/img/" + originalfileName;
        file.transferTo(new File(fullPath));
        System.out.println(title + " " + composer + " " + MkDate + " " + gender + " " + time + " " + originalfileName + " " + TjNum + " " + link + " " + category);
        return "/";
    }
}
