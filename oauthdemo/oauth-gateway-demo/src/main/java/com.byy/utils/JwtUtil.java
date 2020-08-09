/*
package com.byy.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;

public class JwtUtil {
    protected static final Logger log = LoggerFactory.getLogger(JwtUtil.class);
    */
/**
     * JWT校验工具类
     * iss: jwt签发者;
     * sub: jwt所面向的分组用户;
     * aud: 接收jwt的一方;
     * exp: jwt的过期时间，这个过期时间必须要大于签发时间;
     * nbf: 定义在什么时间之前，该jwt都是不可用的;
     * iat: jwt的签发时间;
     * tid: jwt的唯一身份标识，主要用来作为一次性token,从而回避重放攻击;
     *
     * @author hyperdai
     * @create 2020-01-05 15:43
     *//*

    */
/**
     * JWT 加解密类型
     *//*

    private static final SignatureAlgorithm JWT_ALG = SignatureAlgorithm.HS256;
    */
/**
     * JWT 生成密钥使用的密码
     *//*

    private static final String JWT_RULE = "byy";

    */
/**
     * JWT 添加至HTTP HEAD中的前缀
     *//*

    private static final String JWT_SEPARATOR = "";

    */
/**
     * 密钥：通过生成 JWT_ALG 和 JWT_RULE 加密生成
     *//*

    private static final Key SECRET = generateKey(JWT_ALG, JWT_RULE);

    */
/**
     * 默认jwt所面向的分组用户
     *//*

    private static final String DEFAULT_SUB = "system";

    */
/**
     * 使用JWT默认方式，生成加解密密钥
     *
     * @param alg 加解密类型
     * @return
     *//*

    private static SecretKey generateKey(SignatureAlgorithm alg) {
        return MacProvider.generateKey(alg);
    }

    */
/**
     * 使用指定密钥生成规则，生成JWT加解密密钥
     *
     * @param alg  加解密类型
     * @param rule 密钥生成规则
     * @return
     *//*

    private static SecretKey generateKey(SignatureAlgorithm alg, String rule) {
        // 将密钥生成键转换为字节数组
        byte[] bytes = Base64.decodeBase64(rule);
        // 根据指定的加密方式，生成密钥
        return new SecretKeySpec(bytes, alg.getJcaName());
    }

    */
/**
     * 构建JWT
     *
     * @param alg      jwt 加密算法
     * @param sub      jwt 面向的用户
     * @param aud      jwt 接收方
     * @param tid      jwt 唯一身份标识
     * @param iss      jwt 签发者
     * @param nbf      jwt 生效日期时间
     * @param duration jwt 有效时间，单位：秒
     * @return JWT字符串
     *//*

    private static String buildJWT(SignatureAlgorithm alg, String sub, String aud, String tid, String iss, Date nbf, Integer duration) {
        // jwt的签发时间
        long iat = System.currentTimeMillis();
        // jwt的过期时间，这个过期时间必须要大于签发时间
        long exp = 0;
        if (duration != null)
            exp = (nbf == null ? (iat + duration * 1000) : (nbf.getTime() + duration * 1000));

        // 获取JWT字符串
        String compact = Jwts.builder()
                .signWith(alg, SECRET) //加密方式
                .setSubject(sub) //说明
                .setAudience(aud) //接收用户
                .setId(tid) //唯一身份标示
                .setIssuer(iss) //签发者信息
                .setNotBefore(nbf)
                .setIssuedAt(new Date(iat)) //签发时间
                .setExpiration(new Date(exp)) //过期时间
                .compact();

        // 在JWT字符串前添加"Bearer "字符串，用于加入"Authorization"请求头
        return JWT_SEPARATOR + compact;
    }

    */
/**
     * 构建JWT
     *
     * @param sub      jwt 面向的用户
     * @param aud      jwt 接收方
     * @param tid      jwt 唯一身份标识
     * @param iss      jwt 签发者
     * @param nbf      jwt 生效日期时间
     * @param duration jwt 有效时间，单位：秒
     * @return JWT字符串
     *//*

    public static String buildJWT(String sub, String aud, String tid, String iss, Date nbf, Integer duration) {
        return buildJWT(JWT_ALG, sub, aud, tid, iss, nbf, duration);
    }

    */
/**
     * 构建JWT
     *
     * @param sub      jwt 面向的用户
     * @param tid      jwt 唯一身份标识，主要用来作为一次性token,从而回避重放攻击
     * @param duration jwt 有效时间，单位：秒
     * @return JWT字符串
     *//*

    public static String buildJWT(String sub, String tid, Integer duration) {
        return buildJWT(sub, null, tid, null, null, duration);
    }

    */
/**
     * 构建JWT
     *
     * @param tid      jwt 唯一身份标识，主要用来作为一次性token,从而回避重放攻击
     * @param duration jwt 有效时间，单位：秒
     * @return JWT字符串
     *//*

    public static String buildJWT(String tid, Integer duration) {
        return buildJWT(DEFAULT_SUB, tid, duration);
    }

    */
/**
     * 构建JWT
     * 使用 UUID 作为 tid 唯一身份标识
     * JWT有效时间 1200 秒，即 20 分钟
     *
     * @param tid jwt 唯一身份标识，主要用来作为一次性token,从而回避重放攻击
     * @return JWT字符串
     *//*

    public static String buildJWT(String tid) {
        return buildJWT(DEFAULT_SUB, tid, 1200);
    }

    */
/**
     * 从 token 中获取 JWT 的 payload 部分
     *
     * @param token token
     * @return {@link Claims}
     *//*

    private static Claims getClaimsFromToken(String token) throws Exception {
        // 移除 JWT 前的前缀字符串
        token = StringUtils.substringAfter(token, JWT_SEPARATOR);
        // 解析 JWT 字符串
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    */
/**
     * 判断token是否有效：判断sub是否在一个分组、判断是否过期
     *
     * @param token token
     * @param sub   subject
     * @return {@link Boolean}
     *//*

    public static Boolean checkJWT(String token, String sub) {
        try {
            Claims claims = getClaimsFromToken(token);
            if (claims == null || !claims.getSubject().equals(sub) ||
                    claims.getExpiration().before(new Date())) return false;
        } catch (ExpiredJwtException e) {
            // 仅仅是token过期异常直接返回false
            return false;
        } catch (Exception e) {
            log.warn("JWT验证出错，错误原因：{}", e.getMessage());
            return false;
        }
        return true;
    }

    */
/**
     * 判断token是否有效：判断sub是否在一个分组、判断是否过期
     *
     * @param token token
     * @return true or false
     *//*

    public static Boolean checkJWT(String token) {
        return checkJWT(token, DEFAULT_SUB);
    }

    */
/**
     * 从 token 中获取 JWT 的 payload 的 id 内容
     *
     * @param token token
     * @return id value
     *//*

    public static String getJwtID(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return claims.getId();
        } catch (Exception e) {
            log.warn("JWT验证出错，错误原因：{}", e.getMessage());
            return null;
        }
    }


    public static void main(String[] args) {
        String token = JwtUtil.buildJWT("1");

        System.out.println(token);
        System.out.println(checkJWT(token));
        System.out.println(getJwtID(token));
    }
}
*/
