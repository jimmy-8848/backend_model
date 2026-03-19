package com.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.entity.dto.Account;
import com.example.service.AccountService;
import com.example.service.ImageService;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;
@Slf4j
@Service
public class ImageServiceImpl implements ImageService {
    @Resource
    AccountService accountService;
    @Resource
    MinioClient client;

    @Override
    public void getImageFromMinio(OutputStream stream, String image) throws Exception {
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket("study")
                .object(image)
                .build();
        try( GetObjectResponse response = client.getObject(args)){
            //还有帮你写到前端的操作
            IOUtils.copy(response,stream);
        }
    }

    @Override
    public String upload(MultipartFile file, int id) throws IOException{
        String name = "/avatar/"+UUID.randomUUID().toString().replace("-","");
        PutObjectArgs put = PutObjectArgs.builder()
                .object(name)
                .bucket("study")
                .stream(file.getInputStream(),file.getSize(),-1)
                .build();
        try{
            client.putObject(put);
            //没更新，需要更新到表中
            if(accountService.update(null, Wrappers.<Account>update().eq("id",id).set("avatar",name))){
                return name;
            }else{
                return null;
            }
        }catch (Exception e){
            log.warn("头像上传失败："+e.getMessage());
            return null;
        }
    }
}
