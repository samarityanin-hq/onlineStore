package main.store.Services;

import jakarta.persistence.EntityNotFoundException;
import main.store.DTOs.ProductOut;
import main.store.Repositories.ProductRepo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class ProductService {

    private final ProductRepo productRepo;

    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @Cacheable(value = "products", key = "#title")
    public ProductOut getProduct(String title){

        return productRepo
                .findProductDTO(title)
                .orElseThrow(() -> new EntityNotFoundException("Product with title %s doesnt exist".formatted(title)));
    }

    public Page<ProductOut> getProductsCatalog(Pageable pageable){
        return productRepo.findDTOList(pageable);
    }

}
