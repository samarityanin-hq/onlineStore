package main.store.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import main.store.DTO.Request.UserCredentials;
import main.store.DTO.Response.AccessToken;
import main.store.DTO.Response.JwtAuthentication;
import main.store.DTO.Response.UserOut;
import main.store.Security.CustomUserDetails;
import main.store.Services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@Tag(name = "Проверка сессии")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Value("${jwt.refresh-ttl}")
    private Duration refreshTtl;
    private final String REFRESH_COOKIE = "refreshToken";

    @Operation(summary = "Логин юзера")
    @PostMapping("/login")
    public ResponseEntity<AccessToken> login(
            @Valid @RequestBody UserCredentials credentials
    ){
        JwtAuthentication tokens = userService.login(credentials);
        ResponseCookie cookie = buildRefreshCookie(tokens.refreshToken());

        log.info("Called method login");
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new AccessToken(tokens.accessToken()));
    }

    @Operation(summary = "Обновить токен")
    @PostMapping("/refreshToken")
    public ResponseEntity<AccessToken> refreshToken(
            @CookieValue(name = REFRESH_COOKIE,  required = false) String oldRefreshToken
            ){
        JwtAuthentication tokens = userService.refreshToken(oldRefreshToken);
        ResponseCookie cookie = buildRefreshCookie(tokens.refreshToken());

        log.info("called method refreshToken");
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new AccessToken(tokens.accessToken()));
    }

    @Operation(summary = "Логаут")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = REFRESH_COOKIE,  required = false) String refreshToken
            ){
        userService.logout(refreshToken);
        ResponseCookie expired =
                ResponseCookie.from(REFRESH_COOKIE, "")
                        .httpOnly(true)
                        .secure(true)
                        .path("/auth")
                        .maxAge(0)
                        .build();

        log.info("called method logout");
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, expired.toString())
                .build();
    }

    @Operation(summary = "Проверка jwt после аутентификации")
    @GetMapping("/me")
    public ResponseEntity<UserOut> getCurrentUser(
            @AuthenticationPrincipal CustomUserDetails userDetails){

        log.info("called method getCurrentUser");
        return ResponseEntity
                .ok()
                .body(userService.getCurrentUser(userDetails));
    }

    private ResponseCookie buildRefreshCookie(String value){
        return ResponseCookie.from(REFRESH_COOKIE, value)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/auth")
                .maxAge(refreshTtl)
                .build();
    }

}
