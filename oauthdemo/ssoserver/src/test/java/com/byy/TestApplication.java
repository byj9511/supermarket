package com.byy;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@SpringBootTest
public class TestApplication {
    //保存服务器中用于验证的密码
    private static final String secretString = "Vu0iqNhXCFIKpWPNw2Q0qp8I5phreZ4xjUWqvVKLUgI";
    private String token;

    @Before
    public void JWTDemo1() {
        SecretKey secretKey = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
        String jws = Jwts.builder()
                .setIssuer("me")
                .setSubject("Bob")
                .setAudience("you")
                .setIssuedAt(new Date()) // for example, now
                .setSubject("Joe")
                .signWith(secretKey)
                .compact();
        token = jws;
    }

    @Test
    public void JWTDemo2() {
        System.out.println(token);
        SecretKey secretKey = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
        Jws<Claims> jws=null;
        jws = Jwts.parserBuilder()  // (1)
                .requireSubject("Joe")
                .setSigningKey(secretKey)         // (2)
                .build()                    // (3)
                .parseClaimsJws(token); // (4)
        System.out.println(jws);
    }
}
