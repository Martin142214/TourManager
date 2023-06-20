package com.example.TourManager.Services;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.TourManager.FileUploadUtils;

@Service
public class UploadImageService {
    
    @Autowired
    public UploadImageService() {
        
    }

    public void uploadImage(MultipartFile image, String imageName, File newDirectory) {
        try {
            FileUploadUtils.saveFile(newDirectory.getAbsolutePath(), imageName, image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

public String concatenate(String... s)
{   
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s.length; i++) {
        sb = sb.append(s[i]);
    }

    return sb.toString();
}
}
