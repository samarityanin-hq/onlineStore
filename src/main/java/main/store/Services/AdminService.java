package main.store.Services;

import main.store.DTOs.CategoryList;
import main.store.DTOs.CategoryOut;
import main.store.DTOs.ProductOut;
import main.store.DTOs.ProductToAdd;
import main.store.Entities.Category;
import main.store.Entities.Product;
import main.store.Repositories.CategoryRepo;
import main.store.Repositories.OrderRepo;
import main.store.Repositories.ProductRepo;
import main.store.Repositories.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final CategoryRepo categoryRepo;

    public AdminService(UserRepo userRepo, ProductRepo productRepo, OrderRepo orderRepo, CategoryRepo categoryRepo) {
        this.userRepo = userRepo;
        this.productRepo = productRepo;
        this.orderRepo = orderRepo;
        this.categoryRepo = categoryRepo;
    }


    public void addProduct(ProductToAdd product){
        Category category = categoryRepo.getReferenceById(product.categoryId());
        Product newProduct = new Product(product, category);
        productRepo.save(newProduct);
    }

    public CategoryList getCategories() {
        List<Category> categories = categoryRepo.findAll();
        
        return new CategoryList(categories
                .stream()
                .map(this::convertToCategoryOut)
                .toList());
    }

    private CategoryOut convertToCategoryOut(Category category){
        return new CategoryOut(category.getId(),
                category.getName());
    }
}
