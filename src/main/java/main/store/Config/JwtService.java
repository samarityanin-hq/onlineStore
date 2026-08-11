package main.store.Config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import main.store.DTO.Response.JwtAuthentication;
import main.store.Entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtService {

    private static final String TYPE_ACCESS = "access";
    private static final String TYPE_REFRESH = "refresh";
    private static final String REFRESH_KEY_PREFIX = "refresh:";

    @Value("${jwt.access-ttl}")
    private Duration accessTtl;
    @Value("${jwt.refresh-ttl}")
    private Duration refreshTtl;
    @Value("${JWT_SECRET}")
    private String jwtSecret;

    private static final Logger LOGGER = LogManager.getLogger(JwtService.class);

    private final StringRedisTemplate redisTemplate;

    public JwtAuthentication generateAuthToken(User user){
        return new JwtAuthentication(
                generateAccessToken(user),
                generateRefreshToken(user.getEmail())
        );
    }

    public JwtAuthentication refreshAuthToken(User user, String oldRefreshToken){
        String oldJti = getClaim(oldRefreshToken, "jti", String.class);
        redisTemplate.delete(REFRESH_KEY_PREFIX + oldJti);

        return new JwtAuthentication(
                generateAccessToken(user),
                generateRefreshToken(user.getEmail())
        );
    }



    public void logout(String refreshToken){
        String jti = getClaim(refreshToken, "jti", String.class);
        redisTemplate.delete(REFRESH_KEY_PREFIX+jti);
    }

    public String getEmailFromToken(String token){
        return parseClaims(token).getSubject();
    }

    public String getRoleFromToken(String token){
        return getClaim(token, "tole", String.class);
    }

    public boolean isAccessToken(String token){
        return TYPE_ACCESS.equals(getClaim(token, "type", String.class));
    }

    public boolean isRefreshToken(String token){
        String jti = getClaim(token, "jti", String.class);
        return TYPE_REFRESH.equals(getClaim(token, "type", String.class))
                && jti != null
                && redisTemplate.hasKey(REFRESH_KEY_PREFIX + jti);
    }

    public boolean validateJwtToken(String token){
        try {
            parseClaims(token);
            return true;
        }catch (ExpiredJwtException exception){
            LOGGER.info("Token expired: {}", exception.getMessage());
        }catch (UnsupportedJwtException exception){
            LOGGER.warn("Unsupported token: {}", exception.getMessage());
        }catch (MalformedJwtException exception){
            LOGGER.warn("Malformed token: {}", exception.getMessage());
        }catch (SecurityException exception){
            LOGGER.warn("Invalid token signature: {}", exception.getMessage());
        }catch (Exception exception){
            LOGGER.error("Unexpected error: {}", exception.getMessage());
        }
        return false;
    }

    private String generateAccessToken(User user) {
        Date expiration = toDate(LocalDateTime.now().plus(accessTtl));

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("type", TYPE_ACCESS)
                .claim("role", user.getRole().name())
                .expiration(expiration)
                .signWith(getSignKey())
                .compact();
    }

    private String generateRefreshToken(String email){
        String jti = UUID.randomUUID().toString();
        Date expiration = toDate(LocalDateTime.now().plus(refreshTtl));

        String token = Jwts.builder()
                .subject(email)
                .id(jti)
                .claim("type", TYPE_REFRESH)
                .expiration(expiration)
                .signWith(getSignKey())
                .compact();

        redisTemplate.opsForValue().set(REFRESH_KEY_PREFIX + jti, email, refreshTtl);
        return token;
    }

    private <T> T getClaim(String token, String name, Class<T> type){
        return parseClaims(token).get(name, type);
    }

    private Claims parseClaims(String token){
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    private Date toDate(LocalDateTime dateTime){
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private SecretKey getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
