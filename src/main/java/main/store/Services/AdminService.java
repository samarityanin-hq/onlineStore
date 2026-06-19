package main.store.Services;

import main.store.DTOs.CategoryList;
import main.store.DTOs.CategoryOut;
import main.store.DTOs.ProductOut;
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


    public void addProduct(ProductOut productOut){
        //Product newProduct = new Product()
    }

    public CategoryList getCategories() {
        List<Category> categories = categoryRepo.getAll();
        
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
