package main.store.Exceptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import main.store.DTO.Response.ExceptionResponse;
import main.store.Exceptions.CustomExceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    String exceptionStr = "Handle exception: ";


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgExc(IllegalArgumentException e){
        log.warn(exceptionStr, e);
        return ResponseEntity
                .status(400)
                .body(new ExceptionResponse(400,
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgNotValidExc(MethodArgumentNotValidException e){
        String errorFields = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(java.util.stream.Collectors.joining("; "));

        log.warn(exceptionStr, e);
        return ResponseEntity
                .status(400)
                .body(new ExceptionResponse(400,
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        errorFields));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentMismatchExc(MethodArgumentTypeMismatchException e){
        String message = String.format("Param %s must be number", e.getName());
        log.warn(exceptionStr, e);
        return ResponseEntity
                .status(400)
                .body(new ExceptionResponse(400,
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        message));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentialsExc(BadCredentialsException e){
        log.warn(exceptionStr, e);
        return ResponseEntity
                .status(401)
                .body(new ExceptionResponse(401,
                        HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        e.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationExc(AuthenticationException e){
        log.warn(exceptionStr, e);
        return ResponseEntity
                .status(401)
                .body(new ExceptionResponse(401,
                        HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        e.getMessage()));
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationExc(InvalidTokenException e){
        log.warn(exceptionStr, e);
        return ResponseEntity
                .status(401)
                .body(new ExceptionResponse(401,
                        HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        e.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedExc(AccessDeniedException e){
        log.warn(exceptionStr, e);
        return ResponseEntity
                .status(403)
                .body(new ExceptionResponse(403,
                        HttpStatus.FORBIDDEN.getReasonPhrase(),
                        e.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFoundExc(EntityNotFoundException e){
        log.warn(exceptionStr, e);
        return ResponseEntity
                .status(404)
                .body(new ExceptionResponse(404,
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        e.getMessage()));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleUserAlreadyExistsExc(UserAlreadyExistsException e){
        log.warn(exceptionStr, e);
        return ResponseEntity
                .status(409)
                .body(new ExceptionResponse(409,
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        e.getMessage()));
    }

    @ExceptionHandler(ProductOutOfStockException.class)
    public ResponseEntity<ExceptionResponse> handleProductOutOfStockExc(ProductOutOfStockException e){
        log.warn(exceptionStr, e);
        return ResponseEntity
                .status(409)
                .body(new ExceptionResponse(409,
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        e.getMessage()));
    }

    @ExceptionHandler(OrderAlreadyPaidException.class)
    public ResponseEntity<ExceptionResponse> handleOrderAlreadyPaidExc(OrderAlreadyPaidException e){
        log.warn(exceptionStr, e);
        return ResponseEntity
                .status(409)
                .body(new ExceptionResponse(409,
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        e.getMessage()));
    }

    @ExceptionHandler(InvalidPaymentAmountException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidPaymentAmountExc(InvalidPaymentAmountException e){
        log.warn(exceptionStr, e);
        return ResponseEntity
                .status(409)
                .body(new ExceptionResponse(409,
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        e.getMessage()));
    }

    @ExceptionHandler(EmptyCartException.class)
    public ResponseEntity<ExceptionResponse> handleEmptyCartExc(EmptyCartException e){
        log.warn(exceptionStr, e);
        return ResponseEntity
                .status(409)
                .body(new ExceptionResponse(409,
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        e.getMessage()));
    }

    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<ExceptionResponse> handleOptimisticLockExc(OptimisticLockException e){
        log.warn(exceptionStr, e);
        return ResponseEntity
                .status(409)
                .body(new ExceptionResponse(409,
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        "The item was just purchased, please try placing your order again."));
    }

    @ExceptionHandler(TooManyRequestException.class)
    public ResponseEntity<ExceptionResponse> handleToManyRequestExc(TooManyRequestException e){
        log.warn(exceptionStr, e);
        return ResponseEntity
                .status(429)
                .body(new ExceptionResponse(429,
                        HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase(),
                        e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericExc(Exception e){
        String errorId = UUID.randomUUID().toString();
        log.error(exceptionStr, e);
        return ResponseEntity
                .status(500)
                .body(new ExceptionResponse(500,
                        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                        "Internal server error: " + errorId));
    }




}
