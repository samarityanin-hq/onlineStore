package main.store.CustomExceptions;

import lombok.Getter;

@Getter
public class OrderAlreadyPaidException extends RuntimeException{
    private final long orderId;

    public OrderAlreadyPaidException(long orderId){
        super(String.format("Order %s, is already paid", orderId));
        this.orderId = orderId;
    }
}
