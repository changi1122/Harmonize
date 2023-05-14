package com.example.harmonize.controller;

import com.example.harmonize.service.PreferService;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.util.*;


@RestController
@RequestMapping("/api")
public class PreferController {

    @Autowired
    private PreferService preferService;

    @PostMapping("/prefer/get/categories")
    public List<String> GetUsersCategory(@RequestParam("uid") Long uid){
        return preferService.GetPreferCategory(uid);
    }


    @PostMapping("/prefer/save")
    public void SaveCategroy(@RequestParam("uid")Long uid, @RequestParam("data") String data){
        System.out.println(uid+" "+data);


        JSONObject jsonObject = new JSONObject(data);
        JSONArray jsonArray = (JSONArray)jsonObject.get("category");
        System.out.println(jsonArray);

    }

    @PostMapping("/test/JSON")
    public void Test(@RequestBody HashMap<String, Object> model){
        System.out.println(model);
        System.out.println(model.get("uid"));
        System.out.println(model.get("data"));
    }




}

