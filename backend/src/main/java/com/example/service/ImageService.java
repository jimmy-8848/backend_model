package com.example.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;

public interface ImageService {
    public String upload(MultipartFile file,int id) throws IOException;
    public void getImageFromMinio(OutputStream stream,String image) throws Exception;
}
