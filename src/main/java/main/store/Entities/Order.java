package Entities;


import Repositories.Status;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Table(name = "Orders")
@Entity
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

}
