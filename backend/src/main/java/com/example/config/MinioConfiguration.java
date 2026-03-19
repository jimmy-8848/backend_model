package com.example.config;

import io.minio.MinioClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfiguration {
    @Bean
    public MinioClient client(){
        return MinioClient.builder()
                .endpoint("http://172.16.247.130:9000")
                .credentials("minio","password")
                .build();
    }
}
