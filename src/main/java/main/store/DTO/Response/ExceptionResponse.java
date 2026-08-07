package main.store.DTO.Response;

public record ExceptionResponse(
        int status,
        String code,
        String message
) {
}
