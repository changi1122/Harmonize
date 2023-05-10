package com.example.harmonize.utility;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class Files {

    public String SaveFile(MultipartFile file, Long id, Integer type) throws IOException {
        String fileName = id.toString();
        String fullPath, formating, path;
        File file1;
        if(type==0){
            formating = ".png";
            path = String.format("/src/main/resources/img/");
        }else{
            formating = ".m4a";
            path = String.format("/src/main/resources/music/");
        }
        fullPath = System.getProperty("user.dir")+path;
        if (!new File(fullPath).exists())
            new File(fullPath).mkdir();
        file1  = new File(fullPath+fileName+formating);
        file.transferTo(file1);
        return fileName;
    }
}
