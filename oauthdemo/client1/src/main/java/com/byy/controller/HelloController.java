package com.byy.controller;


import com.byy.config.SSOConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.crypto.SecretKey;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
public class HelloController {
    @Autowired
    SSOConfig ssoConfig;
    //保存服务器中用于验证的密码
    private static final String SECRETE_KEY = "Vu0iqNhXCFIKpWPNw2Q0qp8I5phreZ4xjUWqvVKLUgI";
    @GetMapping("/")
    public String HelloPage(@CookieValue(value = "jws",required = false)String jws,
                            @RequestParam(value = "jws",required = false) String jws_,
                            HttpServletRequest httpServletRequest,
                            HttpServletResponse httpServletResponse,
                            Model model) throws IOException {
        System.out.println(jws);
        //8、如果在验证中心得到jws或者以前已经有jws
        String jwsNotEmpty=(!Strings.isEmpty(jws_))?jws_:jws;
        //1、如果有jws就认为通过登录验证
        if (!Strings.isEmpty(jwsNotEmpty)){
            //进行jws验证
            SecretKey secretKey = Keys.hmacShaKeyFor(SECRETE_KEY.getBytes(StandardCharsets.UTF_8));
            try {
                Jwts.parserBuilder()  // (1)
                        //填写登陆用户名
                        .requireSubject("Joe")
                        //填写服务器端的secretKey
                        .setSigningKey(secretKey)         // (2)
                        .build()                    // (3)
                        .parseClaimsJws(jwsNotEmpty); // (4)
                model.addAttribute("loginUser","张三");
                //9、将token加入到cookie当中
                Cookie cookie = new Cookie("jws", jwsNotEmpty);
                httpServletResponse.addCookie(cookie);
                model.addAttribute("loginUser","张三");
                return "index";
            }
            catch (Exception e){
                StringBuffer requestURL = httpServletRequest.getRequestURL();
                String redirectPath=ssoConfig.getDomain()+ssoConfig.getLoginPath()+"?redirectPth="+requestURL;
                httpServletResponse.sendRedirect(redirectPath);
            }
        }
        else {
            //2、没有cookie并且请求行也没有对于的JWS参数就进行转发
            //3、让浏览器携带client1的地址去请求认证中心
            StringBuffer requestURL = httpServletRequest.getRequestURL();
            String redirectPath=ssoConfig.getDomain()+ssoConfig.getLoginPath()+"?redirectPth="+requestURL;
            httpServletResponse.sendRedirect(redirectPath);
        }
        return null;
    }

}
