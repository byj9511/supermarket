package com.byy.controller;


import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import javax.print.DocFlavor;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

@Controller
public class login {
    //保存服务器中用于验证的密码
    private static final String SECRETE_KEY = "Vu0iqNhXCFIKpWPNw2Q0qp8I5phreZ4xjUWqvVKLUgI";
    @GetMapping("/login")
    public String loginPage(@CookieValue(value = "jws",required = false)String jws,
                            @RequestParam(value ="redirectPth",required = false)String redirectPth,
                            @RequestParam(value = "jws",required = false) String jws_,
                            Model model,
                            HttpServletResponse response) throws IOException {
        //先验证是否有cookie
        //    用redis查询cookie
        String jwsNotEmpty=(!Strings.isEmpty(jws_))?jws_:jws;
        if (!Strings.isEmpty(jwsNotEmpty)){
        //    4、如果登陆过就直接返回，携带上验证中心的cookie
            SecretKey secretKey = Keys.hmacShaKeyFor(SECRETE_KEY.getBytes(StandardCharsets.UTF_8));
            try {
                Jwts.parserBuilder()  // (1)
                        .requireSubject("Joe")
                        .setSigningKey(secretKey)         // (2)
                        .build()                    // (3)
                        .parseClaimsJws(jwsNotEmpty); // (4)
                System.out.println(jws);
                response.sendRedirect(redirectPth+"?jws="+jwsNotEmpty);
                return null;
            }
            catch (Exception e){
                //根据具体的登录异常信息要具体给出提示 TODO
                model.addAttribute("redirectPth",redirectPth);
                return "login";
            }

        }
        else {
            model.addAttribute("redirectPth",redirectPth);
            //5、如果没有登陆过就返回验证中心的登录页面
            return "login";
        }
    }

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @PostMapping("/doLogin")
    public void doLigin(String loginUser,
                          String password,
                          String redirectPth,
                          HttpServletResponse response) throws IOException {
        //6、对登陆的信息进行验证，得到对应的token
        if (loginUser!=null && password !=null){
            System.out.println(loginUser);

            //设置JWSTOKEN
            SecretKey secretKey = Keys.hmacShaKeyFor(SECRETE_KEY.getBytes(StandardCharsets.UTF_8));
            String jws = Jwts.builder()
                    .setIssuer("me")
                    .setSubject("Bob")
                    .setAudience("you")
                    .setIssuedAt(new Date()) // for example, now
                    .setSubject("Joe")
                    .signWith(secretKey)
                    .compact();

            //stringRedisTemplate.opsForValue().set(token, JSON.toJSONString());
            Cookie cookie = new Cookie("ssoServer", jws);
            //7、登陆成功后做两件事情
            //（1）在认证中心域名下设置token
            response.addCookie(cookie);
            //（2）重定向到客户端，并且携带上token信息
            response.sendRedirect(redirectPth+"?jws="+jws);
        }




    }
}
