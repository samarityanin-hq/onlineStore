package main.store.Repositories;

public enum Status {
    CREATED("CREATED"),
    PAID("PAID"),
    DELIVERED("DELIVERED"),
    CANCELED("CANCELED"),
    REFUNDED("REFUNDED");


    private final String status;

    Status(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
