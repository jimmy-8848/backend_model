package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.dto.Account;
import com.example.entity.dto.AccountDetails;
import com.example.entity.vo.request.ChangePwdVO;
import com.example.entity.vo.request.DetailsSaveVO;
import com.example.entity.vo.request.ModifyEmailVO;
import com.example.entity.vo.response.AccountDetailsVO;
import com.example.entity.vo.response.AccountVO;
import com.example.service.AccountDetailsService;
import com.example.service.AccountService;
import com.example.utils.Const;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api/user")
public class AccountController {
    @Resource
    AccountService accountService;
    @Resource
    AccountDetailsService detailsService;
    @RequestMapping("/info")
    public RestBean<AccountVO> info(@RequestAttribute(name = Const.ATTR_USER_ID) int id){
        Account account = accountService.findAccountById(id);
        return RestBean.success(account.asViewObject(AccountVO.class));
    }
    @GetMapping("/details")
    public RestBean<AccountDetailsVO> details(@RequestAttribute(name = Const.ATTR_USER_ID) int id){
        AccountDetails details = Optional.ofNullable(detailsService.findAccountDetailsById(id))
                .orElseGet(AccountDetails::new);
        return RestBean.success(details.asViewObject(AccountDetailsVO.class));
    }
    @PostMapping("/save-details")
    public RestBean<Void> saveDetails(@RequestAttribute(name = Const.ATTR_USER_ID) int id,
                                      @RequestBody @Valid DetailsSaveVO detailsSaveVO){
        boolean success = detailsService.saveAccountDetails(id, detailsSaveVO);
        return success ? RestBean.success() : RestBean.failure(400,"该用户名已被注册，请更换！");

    }
    @PostMapping("/modify-email")
    public RestBean<Void> saveDetails(@RequestAttribute(name = Const.ATTR_USER_ID) int id,
                                      @RequestBody @Valid ModifyEmailVO vo){
        return this.messageHandle(()-> accountService.modifyEmail(id, vo));
    }
    @PostMapping("/change-password")
    public RestBean<Void> changePassword(@RequestAttribute(name = Const.ATTR_USER_ID) int id,
                                         @RequestBody @Valid ChangePwdVO vo){
        return this.messageHandle(()->accountService.changePassword(id,vo));
    }
    private RestBean<Void> messageHandle(Supplier<String> action){
        String message = action.get();
        return message == null ?  RestBean.success(): RestBean.failure(400, message);
    }
}
