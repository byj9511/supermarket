package com.byy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.annotation.Resource;
import java.security.KeyPair;

@Configuration
public class TokenConfig {
    private final String SECRET_KEY="byy";
    //令牌存储方式


    @Resource
    private KeyProperties keyProperties;

    @Bean
    public TokenStore jwtTokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey(SECRET_KEY);//配置JWT使用的秘钥
        //accessTokenConverter.setKeyPair(keyPair());
        return accessTokenConverter;
    }
    private KeyPair keyPair() {
        return new KeyStoreKeyFactory(new ClassPathResource("mytest.jks"), "mypass".toCharArray()).getKeyPair("mytest");
    }
}
