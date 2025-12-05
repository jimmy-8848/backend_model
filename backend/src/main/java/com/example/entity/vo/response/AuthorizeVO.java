package com.example.entity.vo.response;

import lombok.Data;

import java.util.Date;

@Data
public class AuthorizeVO {
    String token;
    String role;
    String username;
    Date expireTime;
}
