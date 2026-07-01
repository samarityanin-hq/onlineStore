package main.store.Controllers;

import main.store.DTO.DTOout.UserOut;
import main.store.Security.CustomUserDetails;
import main.store.Services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserOut> getCurrentUser(
            @AuthenticationPrincipal CustomUserDetails userDetails){

        log.info("called method getCurrentUser");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getCurrentUser(userDetails));
    }

}
