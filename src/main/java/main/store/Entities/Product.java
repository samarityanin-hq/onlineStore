package main.store.Entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import main.store.DTO.DTOin.ProductToAdd;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "products")
public class Product {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "storage_quantity")
    private Integer storageQuantity;

    @ManyToOne
    @JoinColumn(name = "category")
    private Category category;

    public Product(){}
    public Product(ProductToAdd product, Category category){
        title = product.title();
        price = product.price();
        storageQuantity = product.quantity();
        this.category = category;

    }

}
