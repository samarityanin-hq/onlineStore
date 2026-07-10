package main.store.Enums;

import lombok.Getter;

@Getter
public enum HttpCodeResponse {
    Conflict("Conflict"),
    BadRequest("Bad Request"),
    NotFoud("Not Found"),
    InternalServerError("Internal Server Error"),
    Forbidden("Forbidden"),
    Unauthorized("Unauthorized");

    private final String code;

    HttpCodeResponse(String code) {
        this.code = code;
    }
}
