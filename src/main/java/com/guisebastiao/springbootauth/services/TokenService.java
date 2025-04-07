package com.guisebastiao.springbootauth.services;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.guisebastiao.springbootauth.exceptions.RequiredAuthenticationException;
import com.guisebastiao.springbootauth.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${auth.jwt.token.secret}")
    private String secretToken;

    @Value("${session.expiration.time}")
    private String durationToken;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secretToken);

            return JWT.create()
                    .withIssuer("spring-boot-auth")
                    .withSubject(user.getId().toString())
                    .withExpiresAt(this.generateExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RequiredAuthenticationException("Please login again");
        }
    }

    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secretToken);

            return JWT.require(algorithm)
                    .withIssuer("spring-boot-auth")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    private Instant generateExpirationDate() {
        int jwtDuration = Integer.parseInt(this.durationToken);
        return LocalDateTime.now().plusHours(jwtDuration).toInstant(ZoneOffset.UTC);
    }
}
