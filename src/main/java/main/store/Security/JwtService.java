package main.store.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import main.store.DTO.Request.JwtAuthentication;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtService {

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    private static final Logger LOGGER = LogManager.getLogger(JwtService.class);

    public JwtAuthentication generateAuthToken(String email){
        return new JwtAuthentication(
                generateJwtToken(email),
                generateRefreshToken(email)
        );
    }

    public JwtAuthentication refreshBaseToken(String email, String refreshToken){
        return new JwtAuthentication(
                generateJwtToken(email),
                refreshToken
        );
    }

    public String getEmailFromToken(String token){
        Claims claims = Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

    public boolean validateJwtToken(String token){
        try {
            Jwts.parser()
                    .verifyWith(getSignKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return true;
        }catch (ExpiredJwtException exception){
            LOGGER.info("Expired JwtException", exception);
        }catch (UnsupportedJwtException exception){
            LOGGER.error("Unsupported jwtException", exception);
        }catch (MalformedJwtException exception){
            LOGGER.error("Malformed JwtException", exception);
        }catch (SecurityException exception){
            LOGGER.error("Security Exception", exception);
        }catch (Exception exception){
            LOGGER.error("Invalid token", exception);
        }
        return false;
    }

    private String generateJwtToken(String email) {
        Date date = Date.from(LocalDateTime.now()
                .plusMinutes(1)
                .atZone(ZoneId.systemDefault())
                .toInstant());
        return Jwts.builder()
                .subject(email)
                .claim("type", "access")
                .expiration(date)
                .signWith(getSignKey())
                .compact();
    }

    private String generateRefreshToken(String email){
        Date date = Date.from(LocalDateTime.now()
                .plusDays(1)
                .atZone(ZoneId.systemDefault())
                .toInstant());
        return Jwts.builder()
                .subject(email)
                .claim("type", "refresh")
                .expiration(date)
                .signWith(getSignKey())
                .compact();
    }

    private SecretKey getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
