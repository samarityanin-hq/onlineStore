package main.store.Entities;


import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cartSequence")
    @SequenceGenerator(name = "cartSequence", sequenceName = "cartItemSequence", allocationSize = 50)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product item;

    @Column(name = "quantity")
    private Integer itemQuantity;

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

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Product getItem() {
        return item;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public BigDecimal getPositionCost() {
        return positionCost;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setItem(Product item) {
        this.item = item;
        calculatePositionCost();
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
        calculatePositionCost();
    }

    public void setPositionCost(BigDecimal positionCost) {
        this.positionCost = positionCost;
    }
}
