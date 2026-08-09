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
import main.store.Exceptions.CustomExceptions.DtoMatchException;
import main.store.Repositories.*;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final CategoryRepo categoryRepo;
    private final CacheManager cacheManager;


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
        User user = userRepo.findByEmail(userToAdmin.email())
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + userToAdmin.email() + " not found"));
        user.setRole(UserRole.ROLE_ADMIN);
    }

    @CacheEvict(value = "products", key = "#newInfo.oldTitle()")
    @Transactional
    public ProductOut updateProduct(Long productId, ProductToUpdate newInfo){
        Product product = productRepo.getProductById(productId)
                .orElseThrow(()-> new EntityNotFoundException("product not found"));

        if (!product.getTitle().equals(newInfo.oldTitle())){
            throw new DtoMatchException(product.getTitle(), newInfo.oldTitle());
        }

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


    @Transactional
    public void deleteProduct(Long productId){
        Product product = productRepo.findById(productId)
                .orElseThrow(()-> new EntityNotFoundException("product not found"));

        productRepo.deleteById(productId);

        Objects.requireNonNull(cacheManager.getCache("products")).evict(product.getTitle());

    }
}
