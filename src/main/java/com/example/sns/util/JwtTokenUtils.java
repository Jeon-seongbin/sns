package com.example.sns.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtTokenUtils {
    public static String generateToken(String userName, String key, long expiredTimeMs){

            Claims claims = Jwts.claims();
            claims.put("UserName", userName);

            return Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis()+expiredTimeMs))
                    .signWith(getKey(key) , SignatureAlgorithm.HS256)
                    .compact();


    }

    public static Key getKey(String key){
        byte [] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
