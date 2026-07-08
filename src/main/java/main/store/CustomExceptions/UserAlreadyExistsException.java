package main.store.CustomExceptions;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException{

    private final String errorCause;
    private final String value;

    public UserAlreadyExistsException(String errorCause, String value){
        super(String.format("User with %s: %s already exists", errorCause, value));
        this.errorCause = errorCause;
        this.value = value;
    }

}
