package main.store.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "order_items")
public class OrderItem {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product item;

    @Column(name = "quantity")
    private Integer itemQuantity;

    @Column(name = "price_at_purchase")
    private BigDecimal priceAtPurchase;

    @Column(name = "position_cost")
    private BigDecimal positionCost;

    public OrderItem(){}
    public OrderItem(Order order, Product item, Integer itemQuantity){
        this.order = order;
        this.item = item;
        this.itemQuantity = itemQuantity;
        this.priceAtPurchase = item.getPrice();
        calculatePositionCost();

    }

    private void calculatePositionCost(){
        if (itemQuantity <= 0){
            throw new IllegalArgumentException("quantity must be positive");
        }
        positionCost = item.getPrice().multiply(BigDecimal.valueOf(itemQuantity));
    }

}
