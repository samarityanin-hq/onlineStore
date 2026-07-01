package main.store.ExceptionHandler;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    String exceptionStr = "Handle exception: ";

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericExc(Exception e){
        log.error(exceptionStr, e);
        return ResponseEntity
                .status(500)
                .body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgExc(IllegalArgumentException e){
        log.error(exceptionStr, e);
        return ResponseEntity
                .status(400)
                .body(e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundExc(EntityNotFoundException e){
        log.error(exceptionStr, e);
        return ResponseEntity
                .status(404)
                .body(e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedExc(AccessDeniedException e){
        log.error(exceptionStr, e);
        return ResponseEntity
                .status(403)
                .body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleArgNotValidExc(MethodArgumentNotValidException e){
        log.error(exceptionStr, e);
        return ResponseEntity
                .status(400)
                .body(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

}
