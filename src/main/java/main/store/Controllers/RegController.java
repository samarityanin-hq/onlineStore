package main.store.Controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import main.store.DTO.DTOout.UserOut;
import main.store.DTO.DTOin.UserRegistration;
import main.store.Services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegController {
    private final UserService userService;

    private static final Logger log = LoggerFactory.getLogger(RegController.class);

    @PostMapping
    public ResponseEntity<UserOut> createUser(
            @Valid @RequestBody UserRegistration user
    ){
        log.info("called method createUser");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(user));
    }
}
