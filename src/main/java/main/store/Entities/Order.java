package main.store.Entities;

import lombok.Getter;
import lombok.Setter;
import main.store.Enums.Status;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Column(name = "total_orderItems")
    private Integer totalOrderItems;

    @Column(name = "created_at")
    LocalDateTime dateTime;

    @Column(name = "paid_at")
    LocalDateTime payDate;

    public Order(){}
    public Order(User user, Status status, BigDecimal totalPrice, Integer totalOrderItems){
        this.user = user;
        this.status = status;
        this.totalPrice = totalPrice;
        this.totalOrderItems = totalOrderItems;
        this.dateTime = LocalDateTime.now();
    }


}
