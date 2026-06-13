package main.store.Repositories;

public enum Status {
    CREATED("CREATED"),
    WAITING_PAYMENT("PROCESSING"),
    PAID("PAID"),
    SHIPPED("SHIPPED"),
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
