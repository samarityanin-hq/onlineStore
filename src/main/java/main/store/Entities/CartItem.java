package Entities;


import jakarta.persistence.*;

@Table(name = "Cart_items")
@Entity
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



}
