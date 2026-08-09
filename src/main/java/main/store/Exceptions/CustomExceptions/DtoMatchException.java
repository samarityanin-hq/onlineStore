package main.store.Exceptions.CustomExceptions;

import lombok.Getter;

@Getter
public class DtoMatchException extends RuntimeException {
    private final String expectedValue;
    private final String actualValue;

    public DtoMatchException(String expectedValue, String actualValue) {
        super(String.format("Expected: %s, actual: %s", expectedValue, actualValue));
        this.expectedValue = expectedValue;
        this.actualValue = actualValue;
    }
}
