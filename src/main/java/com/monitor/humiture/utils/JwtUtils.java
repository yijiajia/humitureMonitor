package com.monitor.humiture.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@ConfigurationProperties(prefix = "humitor.jwt")
@Slf4j
@Data
public class JwtUtils {

    private String secret;
    private long expire;
    private String header;

    /**
     * 生成jwt token
     * @param userId
     * @return
     */
    public String generateToken(Integer userId){
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + expire*1000);
        return Jwts.builder()
                .setHeaderParam("type","JWT")
                .setSubject(userId+"")
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512,secret)
                .compact();
    }

    /**
     * 校验token的正确性，获取jwt有效信息
     */
    public Claims getClaimByToken(String token){
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            log.debug("validate is token error ",e);
            return null;
        }

    }


    /**
     * token是否过期
     * @param expiration
     * @return
     */
    public boolean isTokenExpired(Date expiration){
        return expiration.before(new Date());
    }


}
