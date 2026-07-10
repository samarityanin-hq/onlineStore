package main.store.DTO.DTOout;

public record ExceptionResponse(
        int status,
        String code,
        String message
) {
}
