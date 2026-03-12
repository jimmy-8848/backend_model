package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import lombok.Data;

@Data
@TableName("db_account_privacy")
public class AccountPrivacy implements BaseData {
    @TableId(type = IdType.NONE)
    final Integer id;
    Boolean phone = false;
    Boolean email = false;
    Boolean qq = false;
    Boolean wx = false;
    Boolean gender = false;
}
