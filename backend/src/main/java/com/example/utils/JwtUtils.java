package com.example.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class JwtUtils {
    @Autowired
    StringRedisTemplate redisTemplate;
    @Value("${spring.security.jwt.key}")
    String key;
    @Value("${spring.security.jwt.expire}")
    int expire;
    public String checkToken(String header){
        if(header!=null && header.startsWith("Bearer ")) return header.substring(7);
        return null;
    }
    public Date expireTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR,expire);
        return calendar.getTime();
    }
    public String createToken(UserDetails user,int id){
        Algorithm algorithm = Algorithm.HMAC256(key);
        String username = user.getUsername();
        try{
            return JWT.create()
                    .withJWTId(UUID.randomUUID().toString())
                    .withClaim("id",id)
                    .withClaim("username",username)
                    .withClaim("authorities",user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()))
                    .withExpiresAt(expireTime())
                    .withIssuedAt(new Date())
                    .sign(algorithm);
        }catch (JWTCreationException e){
            return null;
        }
    }

    public boolean inBlackList(String id){
        return redisTemplate.hasKey(Const.BLACK_LIST+id);
    }

    public DecodedJWT resolveToken(String header){
        //关键点1
        String token = checkToken(header);
        if(token == null) return null;
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier verifier = JWT.require(algorithm).build();
        try{
            DecodedJWT verify = verifier.verify(token);
            String id = verify.getId();
            //关键点2
            if(inBlackList(id)) return null;
            //关键点3
            return new Date().after(verify.getExpiresAt()) ? null : verify;
        }catch (JWTVerificationException e){
            return null;
        }
    }
    public boolean deleteToken(String id,Date expireTime){
        if(inBlackList(id)) return false;
        long expire = Math.max(0,expireTime.getTime()-new Date().getTime());
        redisTemplate.opsForValue().set(Const.BLACK_LIST+id,"",expire, TimeUnit.MILLISECONDS);
        return true;
    }
    public boolean invalidateToken(String header){
        String token = checkToken(header);
        if(token == null) return false;
        Algorithm algorithm = Algorithm.HMAC256(key);
        JWTVerifier verifier = JWT.require(algorithm).build();
        try{
            DecodedJWT verify = verifier.verify(token);
            return deleteToken(verify.getId(),verify.getExpiresAt());
        }catch (JWTVerificationException e){
            return false;
        }
    }
    //用到jwt过滤器
    public UserDetails toUser(DecodedJWT jwt){
        Map<String, Claim> claims = jwt.getClaims();
        return User
                .withUsername(claims.get("username").asString())
                .password("********")
                .authorities(claims.get("authorities").asArray(String.class))
                .build();
    }
    public Integer  toId(DecodedJWT jwt){
        Map<String, Claim> claims = jwt.getClaims();
        return claims.get("id").asInt();
    }
}
