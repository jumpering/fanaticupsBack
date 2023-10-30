package org.fanaticups.fanaticupsBack.security.services;

import java.security.Key;
import java.sql.Date;
import java.util.Map;

import org.fanaticups.fanaticupsBack.dao.entities.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    //@Value("$security.jwt.expiration-minutes") ---> esto seria para incluirlo en el application-properties
    //private long EXPIRATION_MINUTES;

    private final static long EXPIRATION_MINUTES = 30;
    private final static String SECRET_KEY = "ClaveMuySecreta";

    public String generateToken(UserEntity userEntity, Map<String, Object> extraClaims){

        Date issuedAt = new Date(System.currentTimeMillis()); 
        Date expiration = new Date( issuedAt.getTime() + (EXPIRATION_MINUTES * 60 * 1000) ); //30 minutes

        Jwts.builder()
            .setClaims(extraClaims) //obligatorio el primero
            .setSubject(userEntity.getEmail())
            .setIssuedAt(issuedAt)
            .setExpiration(expiration)
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
            .signWith(generateKey(), SignatureAlgorithm.HS256)
            .compact();
        return null;
    }

    private Key generateKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
    
}
