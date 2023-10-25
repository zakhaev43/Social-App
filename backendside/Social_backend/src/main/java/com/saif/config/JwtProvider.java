package com.saif.config;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtProvider {

    // Define your secret key as a byte array (for example, 256 bits)
    private static final byte[] SECRET_KEY_BYTES = "your-secret-key-here".getBytes();

    private SecretKey key = new SecretKeySpec(SECRET_KEY_BYTES, "HmacSHA256");

    public String generateToken(Authentication auth) {
        String jwt = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 86400000))
                .claim("email", auth.getName())
                .signWith(key)
                .compact();
        return jwt;
    }

    public String getEmailFromJwtToken(String jwt) {
        jwt = jwt.substring(7);

        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
        String email = String.valueOf(claims.get("email"));

        return email;
    }

    public String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        Set<String> auths = new HashSet<>();

        for (GrantedAuthority authority : collection) {
            auths.add(authority.getAuthority());
        }
        return String.join(",", auths);
    }
}
