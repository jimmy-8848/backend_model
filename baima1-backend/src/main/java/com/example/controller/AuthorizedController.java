package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.request.ConfirmResetVO;
import com.example.entity.vo.request.EmailRegisterVO;
import com.example.entity.vo.request.EmailResetVO;
import com.example.service.AccountService;
import com.github.yulichang.wrapper.segments.Fun;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthorizedController {
    @Resource
    AccountService service;
    @GetMapping("/ask-code")
    //漏洞是注册的验证码可以用来请求密码重置，在获取验证码那里加一下，如果邮箱被注册且是注册操作就不返回验证码就可以
    public RestBean<Void> askVerifyCode(@RequestParam @Email String email,
                                        @RequestParam @Pattern(regexp = "(register|reset)") String type,
                                        HttpServletRequest request){
        return this.messageHandle(() -> service.sendEmailCode(email,type,request.getRemoteAddr()));
    }

    //axios默认发送json格式
    //以后get方法统一用请求参数的格式接收，post方法统一用json方式接收
    @PostMapping("/register")
    public RestBean<Void> register(@RequestBody @Valid EmailRegisterVO vo){
        return this.messageHandle(vo,service::registerEmailAccount);
    }
    @PostMapping("/reset-confirm")
    public RestBean<Void> resetConfirm(@RequestBody @Valid ConfirmResetVO vo){
        return this.messageHandle(vo,service::resetConfirm);
    }
    @PostMapping("/reset-password")
    public RestBean<Void> resetConfirm(@RequestBody @Valid EmailResetVO vo){
        return this.messageHandle(vo,service::resetEmailAccountPassword);
    }

    private <T> RestBean<Void> messageHandle(T vo, Function<T,String> function){
        return this.messageHandle(()->function.apply(vo));
    }
    private RestBean<Void> messageHandle(Supplier<String> action){
        String message = action.get();
        return message == null ?  RestBean.success(): RestBean.failure(400, message);
    }
}
