package main.store.ExceptionHandler;

import jakarta.persistence.EntityNotFoundException;
import main.store.CustomExceptions.*;
import main.store.DTO.DTOout.ExceptionResponse;
import main.store.Enums.HttpCodeResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    String exceptionStr = "Handle exception: ";


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalArgExc(IllegalArgumentException e){
        log.error(exceptionStr, e);
        return ResponseEntity
                .status(400)
                .body(new ExceptionResponse(400, HttpCodeResponse.BadRequest.getCode(), e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgNotValidExc(MethodArgumentNotValidException e){
        String errorFields = e.getFieldError().getField() + ": " + e.getFieldError().getDefaultMessage();

        log.error(exceptionStr, e);
        return ResponseEntity
                .status(400)
                .body(new ExceptionResponse(400, HttpCodeResponse.BadRequest.getCode(), errorFields));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentialsExc(BadCredentialsException e){
        log.error(exceptionStr, e);
        return ResponseEntity
                .status(401)
                .body(new ExceptionResponse(401, HttpCodeResponse.Unauthorized.getCode(), e.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleAuthenticationExc(AuthenticationException e){
        log.error(exceptionStr, e);
        return ResponseEntity
                .status(401)
                .body(new ExceptionResponse(401, HttpCodeResponse.Unauthorized.getCode(), e.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedExc(AccessDeniedException e){
        log.error(exceptionStr, e);
        return ResponseEntity
                .status(403)
                .body(new ExceptionResponse(403, HttpCodeResponse.Forbidden.getCode(), e.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFoundExc(EntityNotFoundException e){
        log.error(exceptionStr, e);
        return ResponseEntity
                .status(404)
                .body(new ExceptionResponse(404, HttpCodeResponse.NotFoud.getCode(), e.getMessage()));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleUserAlreadyExistsExc(UserAlreadyExistsException e){
        log.error(exceptionStr, e);
        return ResponseEntity
                .status(409)
                .body(new ExceptionResponse(409, HttpCodeResponse.Conflict.getCode(), e.getMessage()));
    }

    @ExceptionHandler(ProductOutOfStockException.class)
    public ResponseEntity<ExceptionResponse> handleProductOutOfStockExc(ProductOutOfStockException e){
        log.error(exceptionStr, e);
        return ResponseEntity
                .status(409)
                .body(new ExceptionResponse(409, HttpCodeResponse.Conflict.getCode(), e.getMessage()));
    }

    @ExceptionHandler(OrderAlreadyPaidException.class)
    public ResponseEntity<ExceptionResponse> handleOrderAlreadyPaidExc(OrderAlreadyPaidException e){
        log.error(exceptionStr, e);
        return ResponseEntity
                .status(409)
                .body(new ExceptionResponse(409, HttpCodeResponse.Conflict.getCode(), e.getMessage()));
    }

    @ExceptionHandler(InvalidPaymentAmountException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidPaymentAmountExc(InvalidPaymentAmountException e){
        log.error(exceptionStr, e);
        return ResponseEntity
                .status(409)
                .body(new ExceptionResponse(409, HttpCodeResponse.Conflict.getCode(), e.getMessage()));
    }

    @ExceptionHandler(EmptyCartException.class)
    public ResponseEntity<ExceptionResponse> handleEmptyCartExc(EmptyCartException e){
        log.error(exceptionStr, e);
        return ResponseEntity
                .status(409)
                .body(new ExceptionResponse(409, HttpCodeResponse.Conflict.getCode(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericExc(Exception e){
        log.error(exceptionStr, e);
        return ResponseEntity
                .status(500)
                .body(new ExceptionResponse(500, HttpCodeResponse.InternalServerError.getCode(), e.getMessage()));
    }


}
