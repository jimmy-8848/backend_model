package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Account;
import com.example.entity.dto.AccountDetails;
import com.example.entity.vo.request.DetailsSaveVO;
import com.example.entity.vo.response.AccountDetailsVO;
import com.example.mapper.AccountDetailsMapper;
import com.example.service.AccountDetailsService;
import com.example.service.AccountService;
import jakarta.annotation.Resource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountDetailsServiceImpl extends ServiceImpl<AccountDetailsMapper, AccountDetails> implements AccountDetailsService {
    @Resource
    AccountService service;
    public AccountDetails findAccountDetailsById(int id){
        return this.getById(id);
    }
    @Override
    @Transactional
    public synchronized boolean saveAccountDetails(int id, DetailsSaveVO details) {
        String username = details.getUsername();
        Account account = service.findAccountByEmailOrName(username);
        if(account == null || account.getId()==id){
            try{
                service.update()
                        .eq("id",id)
                        .set("username",username)
                        .update();
            }catch (DuplicateKeyException e){
                return false;
            }
            this.saveOrUpdate(new AccountDetails(id,details.getGender(),details.getPhone(),details.getQq(),details.getWx(), details.getDesc()));
        }
        return true;
    }
}
