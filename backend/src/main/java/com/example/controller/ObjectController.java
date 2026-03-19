package com.example.controller;

import com.example.entity.RestBean;
import com.example.service.ImageService;
import io.minio.errors.ErrorResponseException;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
//获取东西都走这个controller
public class ObjectController {
    @Resource
    ImageService service;
    @GetMapping("/images/avatar/**")
    public void imageFetch(HttpServletRequest request, HttpServletResponse response) throws Exception{
        this.fetchImage(request,response);
    }
    private void fetchImage(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String path = request.getServletPath().substring(7);
        ServletOutputStream stream = response.getOutputStream();
        if(path.length() < 13){
            response.setStatus(404);
            stream.println(RestBean.failure(404,"Not Found").toString());
        }else{
            try{
                service.getImageFromMinio(stream,path);
                response.setHeader("Cache-Control","max-age=2592000");
            }catch (ErrorResponseException e){
                if(e.response().code() == 404){
                    response.setStatus(404);
                    stream.println(RestBean.failure(404,"Not Found").toString());
                }else{
                    log.error("Minio获取图片出现异常:",e.getMessage(),e);
                }
            }
        }
    }
}
