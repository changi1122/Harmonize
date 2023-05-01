package com.example.harmonize.utility;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class Files {

    public String SaveFile(MultipartFile file, Long id) throws IOException {
        String fileName = id.toString();
        String fullPath = "D:/AppP/Harmonize/backend/src/main/resources/img/";
        File file1  = new File(fullPath+fileName+".m4a");
        file.transferTo(file1);
        return fileName;
    }
}
