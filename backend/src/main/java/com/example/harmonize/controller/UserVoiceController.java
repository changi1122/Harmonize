package com.example.harmonize.controller;

import com.example.harmonize.entity.UserVoice;
import com.example.harmonize.service.UserVoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class UserVoiceController {

    private UserVoiceService userVoiceService;

    @Autowired
    public UserVoiceController(UserVoiceService userVoiceService) {
        this.userVoiceService = userVoiceService;
    }

    @PostMapping("/save/uv")
    public ResponseEntity<?> saveUserVoice(@RequestParam("user_id") Long user_id,
                                           @RequestParam("max") Double max,
                                           @RequestParam("min") Double min) {
        try {
            userVoiceService.SaveUserRange(user_id, max, min);
            return new ResponseEntity<>("사용자 음성 범위 저장에 성공하였습니다.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("사용자 음성 범위 저장에 실패하였습니다.", HttpStatus.BAD_REQUEST);
        }
    }
}
