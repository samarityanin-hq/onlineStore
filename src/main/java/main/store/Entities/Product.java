package main.store.Entities;


import jakarta.persistence.*;
import main.store.DTOs.ProductToAdd;

import java.math.BigDecimal;

@Entity
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

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getStorageQuantity() {
        return storageQuantity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setStorageQuantity(Integer storageQuantity) {
        this.storageQuantity = storageQuantity;
    }
}
