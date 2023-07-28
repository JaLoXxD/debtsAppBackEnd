package com.debts.debtsappbackend.security;

import com.debts.debtsappbackend.model.UserPrincipal;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret}")
    private static String secret;
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10 hours in milliseconds

    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        log.info("SECRET {}", secret);
        return Jwts.builder()
                .signWith(getKey("debtsApp512debtsApp512debtsApp512debtsApp512debtsApp512debtsApp512debtsApp512debtsApp512debtsApp512"))
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + EXPIRATION_TIME))
                .claim("roles", getRoles(userPrincipal))
                .claim("cara", "feísima")
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey(secret)).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey(secret)).build().parseClaimsJws(token).getBody();
            return true;
        } catch (ExpiredJwtException e) {
            log.error("expired token");
        } catch (UnsupportedJwtException e) {
            log.error("unsupported token");
        } catch (MalformedJwtException e) {
            log.error("malformed token");
        } catch (SignatureException e) {
            log.error("bad signature");
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            log.error("fail token");
        }
        return false;
    }

    private List<String> getRoles(UserPrincipal principal) {
        return principal.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }

    private Key getKey(String secret){
        byte [] secretBytes = Decoders.BASE64URL.decode(secret);
        return Keys.hmacShaKeyFor(secretBytes);
    }
}