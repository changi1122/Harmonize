package com.example.harmonize.controller;

import com.example.harmonize.service.PreferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PreferController {

    @Autowired
    private PreferService preferService;

    @PostMapping("/user/categories")
    public List<String> GetUsersCategory(@RequestParam("uid") Long uid){
        return preferService.GetPreferCategory(uid);
    }




}
