package com.example.harmonize.controller;

import com.example.harmonize.entity.User;
import com.example.harmonize.service.UserService;
import com.example.harmonize.service.UserVoiceService;
import com.example.harmonize.utility.Security;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @Autowired
    private UserVoiceService userVoiceService;

    @GetMapping("/get/user")
    public ResponseEntity getUser(){
        User user = (User) userService.loadUserByUsername(Security.getCurrentUsername());

        HashMap<String, Object> result = new HashMap<>();
        result.put("username", user.getUsername());
        result.put("password", user.getPassword());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //로그인
    @PostMapping(path = "/login")
    public ResponseEntity login(final HttpServletRequest req,
                                final HttpServletResponse res,
                                @RequestParam("username") String username,
                                @RequestParam("password") String password) throws Exception {

        try {
            List<String> list = userService.tryLogin(username, password);
            Cookie tokenCookie = createTokenCookie(list.get(0), 168 * 60 * 60);
            res.addCookie(tokenCookie);

            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "로그인에 성공하였습니다.");
            result.put("token", list.get(0));
            result.put("uid", list.get(1));
            return new ResponseEntity(result, HttpStatus.OK);
        }
        catch(Exception e) {
            Cookie tokenCookie = createTokenCookie(null, 0);
            res.addCookie(tokenCookie);

            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "로그인에 실패하였습니다.");
            return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
        }
    }

    //로그아웃
    @GetMapping(path = "/logout")
    public ResponseEntity logout(final HttpServletRequest req, final HttpServletResponse res) {
        Cookie tokenCookie = createTokenCookie(null, 0);
        res.addCookie(tokenCookie);

        HashMap<String, Object> result = new HashMap<>();
        result.put("result", "로그아웃에 성공하였습니다.");
        return new ResponseEntity(result, HttpStatus.OK);
    }

    //회원가입
    @PostMapping("/register")
    public ResponseEntity createUser(@RequestParam("username") String username,
                                     @RequestParam("password") String password) {
        if (username != null && !username.isBlank() &&
                password != null && !password.isBlank()) {

            try {
                userService.create(
                        username,
                        password
                );

                HashMap<String, Object> result = new HashMap<>();

                result.put("result", "회원가입에 성공하였습니다.");
                return new ResponseEntity(result, HttpStatus.CREATED);
            }
            catch (Exception e) {
                HashMap<String, Object> result = new HashMap<>();
                result.put("result", "회원가입에 실패하였습니다.");
                return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
            }
        }
        else {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "회원가입에 실패하였습니다.");
            return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
        }
    }

    //성별(gender) & 연령대(age)
    @PostMapping("/GenderAgeSurvey")
    public ResponseEntity updateGenderAge(@RequestParam("username") String username,
                                            @RequestParam("gender") int gender,
                                            @RequestParam("age") String age) {
        if (username != null && !username.isBlank() &&
                gender != 0 &&
                age != null && !age.isBlank()) {

            try {
                userService.update(
                        username,
                        gender,
                        age
                );

                HashMap<String, Object> result = new HashMap<>();

                result.put("result", "성별/연령대 입력에 성공하였습니다.");
                return new ResponseEntity(result, HttpStatus.CREATED);
            }
            catch (Exception e) {
                HashMap<String, Object> result = new HashMap<>();
                result.put("result", "성별/연령대 입력에 실패하였습니다.");
                return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
            }
        }
        else {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "성별/연령대 입력에 실패하였습니다.");
            return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/checkforduplicate")
    @ResponseBody
    public ResponseEntity CanUseAsUsername(@RequestBody Map<String, String> body) {
        if (body.get("username") != null && !body.get("username").isBlank() &&
                userService.canUseAsUsername(body.get("username"))) {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", true);
            return new ResponseEntity(result, HttpStatus.OK);
        }
        else {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", false);
            return new ResponseEntity(result, HttpStatus.OK);
        }
    }

    private Cookie createTokenCookie(String token, int age) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(age);
        cookie.setPath("/");
        return cookie;
    }
}
