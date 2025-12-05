package com.example.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Account;
import com.example.entity.vo.request.ConfirmResetVO;
import com.example.entity.vo.request.EmailRegisterVO;
import com.example.entity.vo.request.EmailResetVO;
import com.example.mapper.AccountMapper;
import com.example.service.AccountService;
import com.example.utils.Const;
import com.example.utils.FlowUtils;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {
    //Const.VERIFY_EMAIL_DATA+email最好整合成一个方法
    @Resource
    AmqpTemplate amqpTemplate;
    @Autowired
    StringRedisTemplate redisTemplate;
    @Resource
    FlowUtils flowUtils;
    @Resource
    PasswordEncoder encoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = findAccountByEmailOrName(username);
        //UserDetails绝对不能返回Null
        if(account == null) throw new UsernameNotFoundException("用户名或密码错误");
        return User
                .withUsername(account.getUsername())
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }

    @Override
    public String sendEmailCode(String email, String type, String ip) {
        synchronized (ip.intern()){
            if("reset".equals(type) && !existAccountByEmail(email)) {
                return "该邮箱未注册，无法进行密码重置！";
            }

            // 3. 如果是注册，则必须检查邮箱不能已存在（可选安全策略）
            if("register".equals(type) && existAccountByEmail(email)) {
                return "该邮箱已被注册，无法重复注册！";
            }
            if(!limitFlow(ip)) return "请求频繁，请稍后再试！";
            // 2. 如果是重置密码，则必须检查邮箱是否存在
            int code = new Random().nextInt(899999)+100000;
            Map<String,Object> data = Map.of("email",email,"type",type,"code",code);
            amqpTemplate.convertAndSend("email",data);
            redisTemplate.opsForValue().set(Const.VERIFY_EMAIL_DATA+email,String.valueOf(code),3, TimeUnit.MINUTES);
            return null;
        }
    }

    private boolean limitFlow(String ip){
        String pre = Const.VERIFY_EMAIL_LIMIT+ip;
        return flowUtils.limitOnceCheck(pre,60);
    }

    @Override
    public String registerEmailAccount(EmailRegisterVO vo) {
        String email = vo.getEmail();
        String username = vo.getUsername();
        String code = redisTemplate.opsForValue().get(Const.VERIFY_EMAIL_DATA+email);
        if(code == null) return "请先获取验证码";
        if(!code.equals(vo.getCode())) return "验证码输入错误，请重新输入";
        if(this.existAccountByEmail(email)) return "此电子邮件已被其他用户注册";
        if(this.existAccountByUsername(username)) return "此用户名已被其他人注册，请更换一个用户名";
        //其实也有并发问题，但是数据库有加索引，就不需要再去处理了
        String password = encoder.encode(vo.getPassword());
        Account account = new Account(null, username,password,email,"user",new Date());
        if (this.save(account)) {
            redisTemplate.delete(Const.VERIFY_EMAIL_DATA+email);
            return null;
        }else{
            return "内部错误请联系管理员";
        }
    }

    @Override
    public String resetEmailAccountPassword(EmailResetVO vo) {
        //可以用Bean工具类，这里修改了，本来要在ConfirmResetVo上加一个全参数构造的，然后构造一个新的对象
        //还有可以用继承关系，到时候填vo就好了
        String email = vo.getEmail();
        ConfirmResetVO resetVO = new ConfirmResetVO();
        BeanUtils.copyProperties(vo,resetVO);
        String verify = this.resetConfirm(resetVO);
        if(verify != null) return verify;
        String password = encoder.encode(vo.getPassword());
        boolean update=this.update().eq("email",email).set("password",password).update();
        if(update){
            redisTemplate.delete(Const.VERIFY_EMAIL_DATA+email);
        }
        return null;
    }

    @Override
    public String resetConfirm(ConfirmResetVO vo) {
        String email = vo.getEmail();
        if(existAccountByEmail(email)){
            String code = redisTemplate.opsForValue().get(Const.VERIFY_EMAIL_DATA + email);
            if(code == null) return "请先获取验证码";
            if(!code.equals(vo.getCode())) return "验证码错误，请重新输入";
            return null;
        }
        return "用户不存在";
    }

    private boolean existAccountByEmail(String email){
        return this.baseMapper.exists(Wrappers.<Account>query().eq("email",email));
    }
    private boolean existAccountByUsername(String username){
        return this.baseMapper.exists(Wrappers.<Account>query().eq("username",username));
    }
    @Override
    public Account findAccountByEmailOrName(String username) {
        return this.query()
                .eq("email",username)
                .or()
                .eq("username",username)
                .one();
    }
}
