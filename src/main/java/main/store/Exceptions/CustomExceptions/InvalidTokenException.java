package main.store.Exceptions.CustomExceptions;

import lombok.Getter;

@Getter
public class InvalidTokenException extends RuntimeException {
    private String exception;

    public InvalidTokenException(String exception) {
        super(exception);
        this.exception = exception;

    }
}
