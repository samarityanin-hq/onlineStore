package main.store.Enums;

import lombok.Getter;

@Getter
public enum Status {
    CREATED("CREATED"),
    PAID("PAID"),
    CANCELED("CANCELED");

    private final String status;

    Status(String status) {
        this.status = status;
    }

}
