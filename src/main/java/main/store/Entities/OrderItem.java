package main.store.Entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
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

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Product getItem() {
        return item;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public BigDecimal getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public BigDecimal getPositionCost() {
        return positionCost;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setItem(Product item) {
        this.item = item;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public void setPriceAtPurchase(BigDecimal priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }

    public void setPositionCost(BigDecimal positionCost) {
        this.positionCost = positionCost;
    }
}
