package com.example.config;

import com.example.entity.RestBean;
import com.example.entity.dto.Account;
import com.example.entity.vo.response.AuthorizeVO;
import com.example.filter.JwtAuthenticationFilter;
import com.example.service.AccountService;
import com.example.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfiguration {
    @Resource
    JwtUtils jwtUtils;
    @Resource
    AccountService accountService;
    @Resource
    JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin(conf -> conf
                        .loginProcessingUrl("/api/auth/login")
                        .successHandler(this::onAuthenticationSuccess)
                        .failureHandler(this::onHandle))
                .logout(conf -> conf
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(this::onLogoutSuccess))
                .exceptionHandling(conf->conf
                        .accessDeniedHandler(this::onHandle)
                        .authenticationEntryPoint(this::onHandle))
                .sessionManagement(conf->conf
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(conf->conf
                        .requestMatchers("/api/auth/**","/error")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    void onHandle(HttpServletRequest request, HttpServletResponse response,  Object e) throws IOException, ServletException{
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        if(e instanceof AccessDeniedException accessDeniedException){
            writer.write(RestBean.forbidden(accessDeniedException.getMessage()).toJsonString());
        }else if(e instanceof AuthenticationException authenticationException){
            writer.write(RestBean.unAuthorized(authenticationException.getMessage()).toJsonString());
        }
    }
    void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException{
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        User user = (User) authentication.getPrincipal();
        Account account = accountService.findAccountByEmailOrName(user.getUsername());
        String token = jwtUtils.createToken(user, account.getId());
        AuthorizeVO vo = new AuthorizeVO();
        BeanUtils.copyProperties(user,vo);
        vo.setExpireTime(jwtUtils.expireTime());
        vo.setToken(token);
        writer.write(RestBean.success(vo).toJsonString());
    }
    void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException{
        response.setContentType("application/json;charset=utf-8");
        PrintWriter writer = response.getWriter();
        String authorization = request.getHeader("Authorization");
        if (jwtUtils.invalidateToken(authorization)) {
            writer.write(RestBean.success().toJsonString());
        }else{
            writer.write(RestBean.failure(400,"登出失败！").toJsonString());
        }

    }
}
