package main.store.CustomExceptions;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class InvalidPaymentAmountException extends RuntimeException{
    private final long orderId;
    private final BigDecimal expectedAmount;
    private final BigDecimal actualAmount;

    public InvalidPaymentAmountException(long orderId, BigDecimal expectedAmount, BigDecimal actualAmount){
        super(String.format("For order %s expected: %s, get %s", orderId, expectedAmount, actualAmount));
        this.orderId = orderId;
        this.expectedAmount = expectedAmount;
        this.actualAmount = actualAmount;
    }


}
