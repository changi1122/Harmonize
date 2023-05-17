package com.example.harmonize.controller;

import com.example.harmonize.dtos.MusicDTO;
import com.example.harmonize.service.BookMarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookMarkController {

    @Autowired
    private BookMarkService bookMarkService;

    // user가 찜한 music 저장, Checked 23.05.13
    @PostMapping("/bookmark/set")
    public void SetBookMark(@RequestParam("uid") Long uid, @RequestParam("mid") Long mid){
        bookMarkService.SaveBookMark(uid, mid);
    }

    // user가 찜한 music list 변환, Checked 23.05.13
    @PostMapping("/bookmark/list")
    public List<MusicDTO> GetBookMark(@RequestParam("uid") Long uid){
        return bookMarkService.GetMusicListByUID(uid);
    }


    // user가 해제한 북마크 삭제, Checked 23.05.14
    @PostMapping("/bookmark/delete")
    public void DeleteBookMark(@RequestParam("uid") Long uid, @RequestParam("mid") Long mid){
        bookMarkService.DeleteBookMark(uid, mid);
    }
}
