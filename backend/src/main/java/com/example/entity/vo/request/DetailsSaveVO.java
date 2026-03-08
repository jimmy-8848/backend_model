package com.example.entity.vo.request;

import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class DetailsSaveVO {
    @TableId
    int id;
    String username;
    @Min(0)
    @Max(1)
    int gender;
    @Length(min = 1,max = 11)
    @Pattern(regexp = "^1[3-9]\\d{9}$",
            message = "手机号格式不正确")
    String phone;
    @Length(min=1,max=13)
    String qq;
    @Length(min=1,max=20)
    String wx;
    @Length(min=1,max=200)
    String desc;
}
