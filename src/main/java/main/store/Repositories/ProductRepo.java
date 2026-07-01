package main.store.Repositories;

import main.store.DTOs.ProductOut;
import main.store.Entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, Long> {

    @Query("""
        SELECT new main.store.DTOs.ProductOut(p.title, p.price, p.storageQuantity)
        FROM Product p
        WHERE p.title = :title
    """)
    Optional<ProductOut> findProductDTO(@Param("title") String title);


    @Query("""
    SELECT new main.store.DTOs.ProductOut(p.title, p.price, p.storageQuantity)
        FROM Product p
    """)
    Page<ProductOut> getProductList(Pageable pageable);

    @Query("""
    SELECT new main.store.DTOs.ProductOut(p.title, p.price, p.storageQuantity)
    FROM Product p
    WHERE p.category.name = :categoryName
""")
    Page<ProductOut> findByCategoryName(@Param("categoryName") String categoryName,
                                        Pageable pageable);


}
