package com.example.harmonize.controller;

import com.example.harmonize.entity.User;
import com.example.harmonize.service.UserService;
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
import java.util.Map;

@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @GetMapping("api/get/user")
    public ResponseEntity getUser(){
        User user = (User) userService.loadUserByUsername(Security.getCurrentUsername());

        HashMap<String, Object> result = new HashMap<>();
        result.put("username", user.getUsername());
        result.put("password", user.getPassword());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //로그인
    @PostMapping(path = "/api/login")
    public ResponseEntity login(final HttpServletRequest req,
                                final HttpServletResponse res,
                                @RequestParam("username") String username,
                                @RequestParam("password") String password) throws Exception {

        try {
            String token = userService.tryLogin(username, password);
            Cookie tokenCookie = createTokenCookie(token, 168 * 60 * 60);
            res.addCookie(tokenCookie);

            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "로그인에 성공하였습니다.");
            result.put("cookie", token);
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
    @GetMapping(path = "/api/logout")
    public ResponseEntity logout(final HttpServletRequest req, final HttpServletResponse res) {
        Cookie tokenCookie = createTokenCookie(null, 0);
        res.addCookie(tokenCookie);

        HashMap<String, Object> result = new HashMap<>();
        result.put("result", "로그아웃에 성공하였습니다.");
        return new ResponseEntity(result, HttpStatus.OK);
    }

    /*
    @GetMapping(path = "/api/currentuser")
    public ResponseEntity getCurrentUserData() {
        HashMap<String, Object> result = new HashMap<>();

        String username = Security.getCurrentUsername();

        result.put("username", username);
        result.put("Authorities", Security.getCurrentUserRole());

        try {
            User currentUser = (User)userService.loadUserByUsername(username);
            result.put("role", currentUser.getRole());
            result.put("email", currentUser.getEmail());
            result.put("reliability", currentUser.getReliability());
        } catch (Exception e){
            // 로그인되지 않았거나 오류난 경우
        }

        return new ResponseEntity(result, HttpStatus.OK);
    }
     */

    //회원가입
    @PostMapping("/api/register")
    public ResponseEntity createUser(@RequestParam("username") String username,
                                     @RequestParam("password") String password,
                                     @RequestParam("user_name") String user_name) {
        if (username != null && !username.isBlank() &&
                password != null && !password.isBlank() &&
                user_name != null && !user_name.isBlank()) {

            try {
                userService.create(
                        username,
                        password,
                        user_name
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
/*
    @PutMapping("/api/user/{username}")
    public ResponseEntity updateUser(@PathVariable("username") String username,
                                     @RequestBody Map<String, String> body) {
        if (!username.isBlank() && username.equals(Security.getCurrentUsername()) &&
                body.get("password") != null && !body.get("password").isBlank()) {

            try {
                userService.update(
                        username,
                        body.get("password"),
                        body.get("newPassword"),
                        body.get("email")
                );

                HashMap<String, Object> result = new HashMap<>();
                result.put("result", "회원 정보 수정에 성공하였습니다.");
                return new ResponseEntity(result, HttpStatus.ACCEPTED);
            }
            catch (Exception e) {
                HashMap<String, Object> result = new HashMap<>();
                result.put("result", "회원 정보 수정에 실패하였습니다.");
                return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
            }
        }
        else {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "회원 정보 수정에 실패하였습니다.");
            return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
        }
    }
 */
/*
    @GetMapping(value = "/api/user/{username}/profileImage", produces = MediaType.ALL_VALUE)
    @ResponseBody
    public FileSystemResource getProfileImage(@PathVariable("username") String username) throws IOException {
        String path = System.getProperty("user.dir") +
                String.format("/src/main/resources/static/resource/profile/%s.jpg", username);

        return (new File(path).exists()) ? new FileSystemResource(path) : null;
    }
 */
/*
    @PostMapping("/api/user/{username}/profileImage")
    @ResponseBody
    public ResponseEntity changeProfileImage(@PathVariable("username") String username,
                                             @RequestParam("profileImage") String profileImage,
                                             @RequestParam(value="image", required = false) MultipartFile image) {
        if (!username.isBlank() && username.equals(Security.getCurrentUsername()) &&
                !profileImage.isBlank()) {
            try {
                userService.changeProfileImage(username, profileImage, image);

                HashMap<String, Object> result = new HashMap<>();
                result.put("result", "프로필 이미지 수정에 성공하였습니다.");
                return new ResponseEntity(result, HttpStatus.ACCEPTED);
            }
            catch (Exception e) {
                HashMap<String, Object> result = new HashMap<>();
                result.put("result", "프로필 이미지 수정에 실패하였습니다.");
                return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
            }
        }
        else {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "프로필 이미지 수정에 실패하였습니다.");
            return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
        }
    }

 */
/*
    @DeleteMapping("/api/user/{username}")
    public ResponseEntity deleteUser(@PathVariable("username") String username) {
        if (!username.isBlank() && username.equals(Security.getCurrentUsername())) {
            try {
                userService.delete(username);

                HashMap<String, Object> result = new HashMap<>();
                result.put("result", "회원 정보 삭제에 성공하였습니다.");
                return new ResponseEntity(result, HttpStatus.ACCEPTED);
            }
            catch (Exception e) {
                HashMap<String, Object> result = new HashMap<>();
                result.put("result", "회원 정보 삭제에 실패하였습니다.");
                return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
            }
        }
        else {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "회원 정보 삭제에 실패하였습니다.");
            return new ResponseEntity(result, HttpStatus.BAD_REQUEST);
        }
    }

 */

    @PostMapping("/api/checkforduplicate")
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
    /*
    @PostMapping("/api/findusername")
    @ResponseBody
    public ResponseEntity findUsername(@RequestBody Map<String, String> body) {
        try {
            if (body.get("email") == null || body.get("email").isBlank())
                throw new Exception();

            HashMap<String, Object> result = new HashMap<>();
            result.put("result", userService.findUsernameByEmail(body.get("email")));
            return new ResponseEntity(result, HttpStatus.OK);
        }
        catch (Exception e) {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "");
            return new ResponseEntity(result, HttpStatus.OK);
        }
    }
     */
    /*
    @GetMapping("/api/user/{username}/reliability")
    @ResponseBody
    public ResponseEntity getReliability(@PathVariable("username") String username) {
        HashMap<String, Object> result = new HashMap<>();
        result.put("result", userService.getReliability(username));
        return new ResponseEntity(result, HttpStatus.OK);
    }

     */

    private Cookie createTokenCookie(String token, int age) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(age);
        cookie.setPath("/");
        return cookie;
    }
}
