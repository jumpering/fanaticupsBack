package org.fanaticups.fanaticupsBack.security.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.fanaticups.fanaticupsBack.security.dao.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtUtilService {
  // LLAVE_MUY_SECRETA => [Base64] => TExBVkVfTVVZX1NFQ1JFVEE=
  private static final String JWT_SECRET_KEY = "TExBVkVfTVVZX1NFQ1JFVEE=";

  public static final long JWT_TOKEN_VALIDITY = 1000 * 60 * 60 * (long) 8; // 8 Horas

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    return claimsResolver.apply(extractAllClaims(token));
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(token).getBody();
  }

  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public String generateToken(UserEntity userEntity) {
    Map<String, Object> claims = new HashMap<>();
    // Agregando informacion adicional como "claim"
    GrantedAuthority rol = userEntity.getAuthorities().stream().collect(Collectors.toList()).get(0);
    //String name = userDetails.getUsername(); //TODO no coje el nombre de usuario, sino el mail!
    String name = userEntity.getName();
    Long id = userEntity.getId();
    claims.put("rol", rol);
    claims.put("name", name);
    claims.put("id", id);
    return createToken(claims, userEntity.getUsername());
  }

  private String createToken(Map<String, Object> claims, String subject) {

    return Jwts
        .builder()
        .setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
        .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
        .compact();
  }

  public boolean validateToken(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }
}

