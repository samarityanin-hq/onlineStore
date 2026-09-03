package main.store.Exceptions.CustomExceptions;

public class TooManyRequestException extends RuntimeException {
    private final String exception;

    public TooManyRequestException(String exception) {
        super(exception);
        this.exception = exception;
    }
}
