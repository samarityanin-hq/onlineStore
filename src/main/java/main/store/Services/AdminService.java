package main.store.Services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import main.store.DTO.DTOin.ProductToAdd;
import main.store.DTO.DTOin.UserToAdmin;
import main.store.DTO.DTOout.CategoryList;
import main.store.DTO.DTOout.CategoryOut;
import main.store.Entities.Category;
import main.store.Entities.Product;
import main.store.Entities.User;
import main.store.Enums.UserRole;
import main.store.Repositories.*;
import org.springframework.stereotype.Service;

import java.util.List;

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




}
