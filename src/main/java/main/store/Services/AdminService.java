package main.store.Services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import main.store.DTO.Request.ProductToAdd;
import main.store.DTO.Request.ProductToUpdate;
import main.store.DTO.Request.UserToAdmin;
import main.store.DTO.Response.CategoryList;
import main.store.DTO.Response.CategoryOut;
import main.store.DTO.Response.ProductOut;
import main.store.Entities.Category;
import main.store.Entities.Product;
import main.store.Entities.User;
import main.store.Enums.UserRole;
import main.store.Repositories.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final CategoryRepo categoryRepo;


    public void addProduct(ProductToAdd product){
        Category category = categoryRepo.getReferenceById(product.categoryId());
        Product newProduct = new Product(product, category);
        productRepo.save(newProduct);
    }

    public CategoryList getCategories() {
        List<CategoryOut> categories = categoryRepo.getAllCategories();
        return new CategoryList(categories);
    }

    @Transactional
    public void promoteToAdmin(UserToAdmin userToAdmin) {
        User user = userRepo.findByEmail(userToAdmin.email());
        user.setRole(UserRole.ROLE_ADMIN);
    }

    @CacheEvict(value = "products", key = "#product.title")
    @Transactional
    public ProductOut updateProduct(Long productId, ProductToUpdate newInfo){
        Product product = productRepo.getProductById(productId)
                .orElseThrow(()-> new EntityNotFoundException("product not found"));

        Optional.ofNullable(newInfo.newTitle())
                .ifPresent(product::setTitle);

        Optional.ofNullable(newInfo.newPrice())
                .ifPresent(product::setPrice);

        Optional.ofNullable(newInfo.newQuantity())
                .ifPresent(product::setStorageQuantity);

        return new ProductOut(
                product.getTitle(),
                product.getPrice(),
                product.getStorageQuantity());
    }
}
