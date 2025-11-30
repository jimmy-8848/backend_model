package com.example.entity;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;

public record RestBean<T>(int code, T data, String message) {
    public static <T>RestBean<T> success(T data){
        return new RestBean<>(200,data,"请求成功！");
    }
    public static <Void>RestBean<Void> success(){
        return success(null);
    }
    public static <T>RestBean<T> failure(int code,String message){
        return new RestBean<>(code,null,message);
    }
    public static <Void>RestBean<Void> forbidden(String message){
        return failure(403,message);
    }
    public static <Void>RestBean<Void> unAuthorized(String message){
        return failure(401,message);
    }
    public String toJsonString(){
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }
}
