package main.store.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Контроллер регистрация")
@RestController
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {

    private final UserService userService;

    private static final Logger log = LoggerFactory.getLogger(RegistrationController.class);

    @Operation(summary = "Регистрация пользователя")
    @PostMapping
    public ResponseEntity<UserOut> createUser(
            @Valid @RequestBody UserRegistration user
    ){
        log.info("called method createUser");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.createUser(user));
    }
}
