package com.byy.filter;

import org.junit.platform.commons.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class TokenFilter implements GlobalFilter, Ordered {
    private String[] skipAuthUrls = {"/ljl-auth/oauth/token"};
    //需要从url中获取token
    private String[] urlToken = {"/ljl-server-chat/websocket"};
    private static final Logger log = LoggerFactory.getLogger(GatewayFilter.class);

    private String publicKey;

    @PostConstruct
    private void getPublicKey(){
        ClassPathResource classPathResource = new ClassPathResource("pub.txt");
//        手动读取public key
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(classPathResource.getInputStream()));
            publicKey=bufferedReader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 过滤器
     *
     * @param exchange
     * @param chain
     * @return
     */

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String uri = exchange.getRequest().getURI().getPath();
        System.out.println(uri);
        //跳过不需要验证的路径
        if (null != skipAuthUrls && Arrays.asList(skipAuthUrls).contains(uri)) {
            return chain.filter(exchange);
        }
        //获取token
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (null != urlToken && Arrays.asList(urlToken).contains(uri)) {
            //该方法需要修改
            String tokens[] = exchange.getRequest().getURI().getQuery().split("=");
            token = tokens[1];
        }
        if (StringUtils.isBlank(token)) {
            //没有token
            return returnAuthFail(exchange, "请登陆");
        } else {
            //有token
            try {
                RsaVerifier rsaVerifier = new RsaVerifier(publicKey);
                //解密token并验证签名

                Jwt jwt = JwtHelper.decodeAndVerify(token,rsaVerifier);

                /*ServerHttpRequest oldRequest = exchange.getRequest();
                URI uri = oldRequest.getURI();
                ServerHttpRequest newRequest = oldRequest.mutate().uri(uri).build();
                // 定义新的消息头
                HttpHeaders headers = new HttpHeaders();
                headers.putAll(exchange.getRequest().getHeaders());
                headers.remove("Authorization");
                headers.set("Authorization", jwt.toString());

                newRequest = new ServerHttpRequestDecorator(newRequest) {
                    @Override
                    public HttpHeaders getHeaders() {
                        HttpHeaders httpHeaders = new HttpHeaders();
                        httpHeaders.putAll(headers);
                        return httpHeaders;
                    }
                };

                return chain.filter(exchange.mutate().request(newRequest).build());
                System.out.println(jwt.toString());*/
                log.info("haha");
                //没有抛出异常就代表验证通过，直接放行
                return chain.filter(exchange);

/*            } catch (IOException e) {
                e.printStackTrace();
                return returnAuthFail(exchange, "token超时");*/
            } catch (Exception e) {

                log.warn("{}",e.getMessage());
                e.printStackTrace();
                return returnAuthFail(exchange, "token验签失败");
            }
        }
    }

/**
 * 返回校验失败
 *
 * @param exchange
 * @return
 *
 * */


    private Mono<Void> returnAuthFail(ServerWebExchange exchange,String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        String resultData = "{\"status\":\"-1\",\"msg\":"+message+"}";
        byte[] bytes = resultData.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        //一定要设置消息头，防止乱码
        response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");

        return exchange.getResponse().writeWith(Flux.just(buffer));
    }



    @Override
    public int getOrder() {
        return -201;
    }
}
