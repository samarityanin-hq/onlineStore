package main.store.Controllers;

import jakarta.validation.Valid;
import main.store.DTOs.UserOut;
import main.store.DTOs.UserRegistration;
import main.store.Services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
public class RegController {
    private final UserService userService;

    public RegController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserOut> createUser(
            @Valid @RequestBody UserRegistration user
    ){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(user));
    }
}
