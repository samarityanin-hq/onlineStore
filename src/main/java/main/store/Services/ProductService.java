package main.store.Services;

import jakarta.persistence.EntityExistsException;
import main.store.DTOs.ProductOut;
import main.store.Entities.Product;
import main.store.Repositories.ProductRepo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepo productRepo;

    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @Cacheable(value = "products", key = "#title")
    public ProductOut getProduct(String title){
        Product product = productRepo.findProductByTitle(title);

        if (product == null){
            throw new EntityExistsException("Product not found");
        }

        return convertToProductOut(product);
    }

    @Cacheable(value = "catalog", key = "{}")
    public List<ProductOut> getProductsCatalog(){
        List<Product> catalog = productRepo.findAll();

        return catalog.stream()
                .map(this::convertToProductOut)
                .toList();
    }


    private ProductOut convertToProductOut(Product product){
        return new ProductOut(product.getTitle(),
                product.getPrice(),
                product.getStorageQuantity());
    }

}
