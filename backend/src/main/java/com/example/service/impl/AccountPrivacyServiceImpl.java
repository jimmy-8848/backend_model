package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.AccountPrivacy;
import com.example.entity.vo.request.PrivacySaveVO;
import com.example.entity.vo.response.AccountPrivacyVO;
import com.example.mapper.AccountPrivacyMapper;
import com.example.service.AccountDetailsService;
import com.example.service.AccountPrivacyService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountPrivacyServiceImpl extends ServiceImpl<AccountPrivacyMapper, AccountPrivacy> implements AccountPrivacyService {
    public AccountPrivacy getPrivacyById(int id){
        return Optional.ofNullable(this.query().eq("id",id).one()).orElseGet(()->new AccountPrivacy(id));
    }

    @Override
    public void savePrivacy(int id, PrivacySaveVO vo) {
        String type = vo.getType();
        Boolean status = vo.getStatus();
        AccountPrivacy privacyById = this.getPrivacyById(id);
        switch (type){
            case "phone" -> privacyById.setPhone(status);
            case "qq" -> privacyById.setQq(status);
            case "wx" -> privacyById.setWx(status);
            case "email" -> privacyById.setEmail(status);
            case "gender" -> privacyById.setGender(status);
        }
        this.saveOrUpdate(privacyById);
    }
}
