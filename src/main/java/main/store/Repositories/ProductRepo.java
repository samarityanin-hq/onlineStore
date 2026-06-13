package main.store.Repositories;

import main.store.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product, Long> {
    Product findProductByTitle(String title);

}
