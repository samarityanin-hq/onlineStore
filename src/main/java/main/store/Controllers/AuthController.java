package main.store.Controllers;

import main.store.DTOs.UserOut;
import main.store.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserOut> getCurrentUser(Principal principal){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(userService.getCurrentUser(principal));
    }

}
