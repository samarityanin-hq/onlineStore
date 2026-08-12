package main.store.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import main.store.DTO.Request.UserCredentials;
import main.store.DTO.Response.JwtAuthentication;
import main.store.DTO.Response.RefreshToken;
import main.store.DTO.Response.UserOut;
import main.store.Security.CustomUserDetails;
import main.store.Services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.sql.Ref;

@Tag(name = "Проверка сессии")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Operation(summary = "Логин юзера")
    @PostMapping("/login")
    public ResponseEntity<JwtAuthentication> login(
            @Valid @RequestBody UserCredentials credentials
    ){
        log.info("Called method login");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.login(credentials));
    }

    @Operation(summary = "Обновить токен")
    @PostMapping("/refreshToken")
    public ResponseEntity<JwtAuthentication> refreshToken(
            @RequestBody RefreshToken refreshToken
            ){
        log.info("called method refreshToken");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.refreshToken(refreshToken));
    }

    @Operation(summary = "Логаут")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestBody RefreshToken refreshToken
            ){
        log.info("called method logout");
        userService.logout(refreshToken);
        return ResponseEntity
                .ok()
                .build();
    }

    @Operation(summary = "Проверка jwt после аутентификации")
    @GetMapping("/me")
    public ResponseEntity<UserOut> getCurrentUser(
            @AuthenticationPrincipal CustomUserDetails userDetails){

        log.info("called method getCurrentUser");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getCurrentUser(userDetails));
    }

}
