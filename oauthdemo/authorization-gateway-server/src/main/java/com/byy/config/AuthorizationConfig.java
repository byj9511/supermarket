package com.byy.config;

import com.byy.auth.UserServiceInMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

//授权服务器配置
@Configuration
@EnableAuthorizationServer //开启授权服务
public class AuthorizationConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;

    @Autowired
    UserServiceInMemory userServiceInMemory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //允许表单提交
        security.allowFormAuthenticationForClients()
                //隐藏验证token的终端
                //.checkTokenAccess("isAuthenticated()");
                //暴露
                .checkTokenAccess("permitAll()")
                //url:/oauth/token_key,exposes public key for token verification if using JWT tokens;
                .tokenKeyAccess("permitAll()");
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
/*        // @formatter: off
        clients.inMemory()
                .withClient("client-a") //client端唯一标识
                .secret(passwordEncoder.encode("client-a-secret")) //客户端的密码，这里的密码应该是加密后的
                .accessTokenValiditySeconds(3600)
                .refreshTokenValiditySeconds(10800) //设置刷新令牌失效时间
                .authorizedGrantTypes("authorization_code","password","refresh_token") //授权模式标识
                .scopes("read_user_info") //作用域
                .resourceIds("resource1") //资源id
                .redirectUris("http://localhost:9001/callback") //回调地址
                .autoApprove(true) ;//自动授权配置
        // @formatter: on*/
        clients.withClientDetails(clientDetails());

    }
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
            throws Exception {

        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> delegates = new ArrayList<>();
        delegates.add(accessTokenConverter);
        enhancerChain.setTokenEnhancers(delegates);
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore) //配置令牌存储策略
                .accessTokenConverter(accessTokenConverter)
                .tokenEnhancer(enhancerChain)
//                端点的访问方式
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .userDetailsService(userServiceInMemory);


    }

    @Autowired
    DataSource dataSource;
    @Bean
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }


}