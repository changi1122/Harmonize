package com.example.harmonize.controller;

import com.example.harmonize.service.PreferService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Key;
import java.sql.Array;
import java.sql.SQLOutput;
import java.util.*;


@RestController
@RequestMapping("/api")
public class PreferController {

    @Autowired
    private PreferService preferService;

    // Get Category List which user prefer, Checked 23.05.14
    @PostMapping("/prefer/get/categories")
    public List<String> GetUsersCategory(@RequestParam("uid") Long uid){
        return preferService.GetPreferCategory(uid);
    }



/*    @PostMapping("/test/JSON")
    public void Test(@RequestBody HashMap<String, Object> model){

        ObjectMapper object = new ObjectMapper();

        System.out.println(model);
        System.out.println(model.get("uid"));
        System.out.println(model.get("data"));
        ArrayList<Map<String, Object>> arrayList = (ArrayList<Map<String, Object>>) model.get("data");
        System.out.println(arrayList.get(1).get("category"));
    }*/

    /*
    Data 형식 예제 test/JSON/list
        {
            "uid": 1,
            "data": [
                {
                    "category": "JPOP"
                },
                {
                    "category": "트로트"
                }
            ]
        }
    *
    */

    // Save Category which user checked or changed, Checked 23.05.14
    @PostMapping("/prefer/save")
    public void SetPreferList(@RequestBody HashMap<String, Object> model) {

        String category = (String) model.get("category");
        category = category.substring(1, category.length() - 1);
        String[] splited = category.split(",");

        ArrayList<String> list  = new ArrayList<>();
        for (String c : splited) {
            list.add(c.trim());
        }

        Integer uid = (Integer) model.get("uid");

        preferService.SaveCategoryByID(Long.valueOf(uid), list);
    }

    /*
    Data 형식 예제 test/JSON/list
        {
            "uid": 1,
            "data": [
                "JPOP",
                "트로트",
                "발라드드"
            ]
        }
    *
    */


    // Delete user-selected categories, Checked 23.05.14
    @PostMapping("/prefer/delete")
    public void DeleteSomePrefer(@RequestBody HashMap<String, Object> model){
        Integer uid = (Integer) model.get("uid");
        ArrayList<String> list  = (ArrayList<String>) model.get("category");

        preferService.DeleteCategoryID(Long.valueOf(uid), list);
    }
}

