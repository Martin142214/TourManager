package com.example.TourManager.Models.ClassModels;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

public class CustomMultipartFile implements MultipartFile {

    private byte[] input;

    public CustomMultipartFile(byte[] imgBytes) {
        this.input = imgBytes;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return input;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(input);
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getOriginalFilename() {
        return null;
    }

    @Override
    public long getSize() {
        return input.length;
    }

    @Override
    public boolean isEmpty() {
        return input == null || input.length == 0;
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        try(FileOutputStream fos = new FileOutputStream(dest.getAbsolutePath())) {
            fos.write(input);
        }
    }
    
}
