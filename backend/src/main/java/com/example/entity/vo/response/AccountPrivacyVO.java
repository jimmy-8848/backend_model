package com.example.entity.vo.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class AccountPrivacyVO {
    Boolean phone;
    Boolean email;
    Boolean qq;
    Boolean wx;
    Boolean gender;
}
