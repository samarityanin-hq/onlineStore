package main.store.Entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "cart_items")
public class CartItem {
    @Setter
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cartSequence")
    @SequenceGenerator(name = "cartSequence", sequenceName = "cartItemSequence", allocationSize = 50)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product item;

    @Column(name = "quantity")
    private Integer itemQuantity;

    @Setter
    @Column(name = "position_cost")
    private BigDecimal positionCost;

    public CartItem(){}
    public CartItem(User user, Product item, Integer itemQuantity){
        this.user = user;
        this.item = item;
        this.itemQuantity = itemQuantity;
        this.positionCost = item.getPrice();
    }

    private void calculatePositionCost(){
        if (this.item != null && this.itemQuantity != null){
            positionCost = item.getPrice().multiply(BigDecimal.valueOf(itemQuantity));
        }
        else {
            positionCost = BigDecimal.ZERO;
        }
    }

    public void setItem(Product item) {
        this.item = item;
        calculatePositionCost();
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
        calculatePositionCost();
    }

}
