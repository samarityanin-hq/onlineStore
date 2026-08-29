package main.store.Enums;

import lombok.Getter;

@Getter
public enum Status {
    CREATED("CREATED"),
    PAID("PAID"),
    CANCELED("CANCELED"),
    ON_THE_WAY("ON_THE_WAY"),
    SHIPPED("SHIPPED");

    private final String status;

    Status(String status) {
        this.status = status;
    }

}
