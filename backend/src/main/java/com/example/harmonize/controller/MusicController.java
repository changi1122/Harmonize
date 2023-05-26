package com.example.harmonize.controller;

import com.example.harmonize.dtos.MusicDTO;
import com.example.harmonize.dtos.MusicDetailDTO;
import com.example.harmonize.entity.Music;
import com.example.harmonize.service.MusicService;

import com.example.harmonize.utility.Analyzer;
import com.example.harmonize.utility.Builder;

import com.example.harmonize.utility.Connector;
import com.example.harmonize.utility.Files;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;


@RestController
public class MusicController {


    Connector connector = new Connector();
    Files files = new Files();

    Analyzer analyzer  =new Analyzer();
    @Autowired
    private MusicService musicService;

    // admin page, music save, Checked 23.05.14
    @PostMapping("/music/save")
    public String SaveMusic(@RequestParam("title") String title, @RequestParam("composer") String composer,
                          @RequestParam("gender") String gender, @RequestParam("TjNum") Integer TjNum,
                            @RequestParam("link") String link, @RequestParam("category") String category,
                            @RequestParam("split") String split, @RequestParam("file") MultipartFile file, @RequestParam("music_file")MultipartFile MF) throws IOException {

        Long id =  musicService.MusicSave(title, composer, gender, TjNum, link, category, 0L);
        System.out.println(id);

        String ImgfileName = files.SaveFile(file, id, 0);
        System.out.println(ImgfileName);
        musicService.SaveFile(ImgfileName, id, 0);
        // img 파일 저장

        String MusicfileName = files.SaveFile(MF, id, 1);
        System.out.println(MusicfileName);
        // music 파일 저장

        connector.SocketCall(id+".m4a",  id, Long.parseLong(split));

        // *Need to Add Analyzer Code & save Detail's
        musicService.SaveDetail(id, gender);
        return "/";
    }


    // search artist, music_name like string, Checked 23.05.13
    @PostMapping("/api/music/search")
    public List<Music> SearchMusic(@RequestParam("search") String search){
        return musicService.GetResultBySearch(search);
    }

    // get All music list, Data type is MusicDTO, Checked 23.05.13
    @PostMapping("/api/music/get/list")
    public List<MusicDTO> GetMusicList(@RequestParam("uid") String uid , @RequestParam("cid") String cid ){
        System.out.println("category_id"+cid);
        return musicService.GetListByCategory(Long.valueOf(uid) , Long.valueOf(cid));
    }

    @PostMapping("/api/music/analyzer/test")
    public void TestSet(@RequestParam("mid") String mid, @RequestParam("gender") String gender) throws IOException {
        analyzer.FindMusicRange(mid, gender);
    }

    // get a music detail
    @GetMapping("/api/musics/{id}")
    public MusicDetailDTO GetMusicDetail(@PathVariable("id") String mid)
    {
        Music target = musicService.FindByID(mid);
        Builder builder = new Builder();
        return builder.BuildDetailDTO(target, false, 0);
    }

    // send music album cover
    @GetMapping(value = "/api/music/img/{imgNo}", produces = MediaType.ALL_VALUE)
    public FileSystemResource GetImage(@PathVariable("imgNo") String imgNo){
        String IMG_PATH = System.getProperty("user.dir")+ "/src/main/resources/img/";
        String fileExt = "";
        if (new File(IMG_PATH + imgNo + ".png").exists())
            fileExt = ".png";
        else if (new File(IMG_PATH + imgNo + ".jpg").exists())
            fileExt = ".jpg";
        else if (new File(IMG_PATH + imgNo + ".jpeg").exists())
            fileExt = ".jpeg";

        String path = IMG_PATH + imgNo + fileExt;
        return new FileSystemResource(path);
    }

}
