package main.store.Services;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import main.store.DTO.DTOout.CategoryList;
import main.store.DTO.DTOout.CategoryOut;
import main.store.DTO.DTOout.ProductOut;
import main.store.Repositories.CategoryRepo;
import main.store.Repositories.ProductRepo;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepo productRepo;
    private final CategoryRepo categoryRepo;

    @Cacheable(value = "products", key = "#title")
    public ProductOut getProduct(String title){

        return productRepo
                .findProductDTO(title)
                .orElseThrow(() -> new EntityNotFoundException("Product with title %s doesnt exist".formatted(title)));
    }

    public Page<ProductOut> getProductsCatalog(Pageable pageable){
        return productRepo.getProductList(pageable);
    }

    public CategoryList getCategories() {
        List<CategoryOut> categories = categoryRepo.getAllCategories();

        return new CategoryList(categories);
    }

    public Page<ProductOut> sortByCategory(String categoryName, Pageable pageable) {
        return productRepo.findByCategoryName(categoryName, pageable);

    }
}
