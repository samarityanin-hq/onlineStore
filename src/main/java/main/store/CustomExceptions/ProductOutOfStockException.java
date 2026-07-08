package main.store.CustomExceptions;

import lombok.Getter;

@Getter
public class ProductOutOfStockException extends RuntimeException{
    private final String productName;
    private final long productId;

    public ProductOutOfStockException(String productName, long productId){
        super(String.format("Product name: %s, id: %s, is out of stock", productName, productId));
        this.productName = productName;
        this.productId = productId;
    }
}
