package com.example.controller;

import com.example.entity.RestBean;
import com.example.service.ImageService;
import com.example.utils.Const;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@Slf4j
@RestController
@RequestMapping("/api/images")
public class ImageController {
    @Resource
    ImageService imageService;
    @PostMapping("/avatar")
    public RestBean<String> upload(@RequestParam("file") MultipartFile file,
                                   @RequestAttribute(Const.ATTR_USER_ID) int id) throws IOException {
        //限制上传文件最大不超过100KB 1KB = 1024B
        if(file.getSize() > 1024 * 100) return RestBean.failure(403,"文件不能大于100KB");
        log.info("正在进行头像上传操作...");
        String message = imageService.upload(file, id);
        if(message == null) return RestBean.failure(400,"头图像上传失败,请联系管理员！");
        log.info("头像上传成功,大小："+file.getSize());
        return RestBean.success(message);
    }
}
