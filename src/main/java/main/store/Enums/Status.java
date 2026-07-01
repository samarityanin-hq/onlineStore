package main.store.Enums;

public enum Status {
    CREATED("CREATED"),
    PAID("PAID"),
    CANCELED("CANCELED");

    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
