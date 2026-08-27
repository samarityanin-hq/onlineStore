package main.store.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import main.store.DTO.Request.NewCategory;
import main.store.DTO.Request.ProductToUpdate;
import main.store.DTO.Request.NewProduct;
import main.store.DTO.Request.UserToAdmin;
import main.store.DTO.Response.CategoryOut;
import main.store.DTO.Response.ProductOut;
import main.store.Services.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Контроллер админа", description = "Доступно только пользователям со статусом админа")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    @Operation(summary = "Показать доступные категории")
    @GetMapping("/products/getCategories")
    public ResponseEntity<List<CategoryOut>> getCategories(){
        log.info("called admin method getCategories");
        return ResponseEntity
                .ok()
                .body(adminService.getCategories());
    }

    @Operation(summary = "Создать категорию")
    @PostMapping("/products/createCategory")
    public ResponseEntity<Void> createCategory(
            @Valid @RequestBody NewCategory newCategory
            ){
        log.info("called method create category");
        adminService.createCategory(newCategory);
        return ResponseEntity
                .ok()
                .build();
    }

    @Operation(summary = "Обновить название категории")
    @PatchMapping("/products/updateCategoryName/{categoryId}")
    public ResponseEntity<CategoryOut> updateCategoryName(
            @PathVariable Long categoryId,
            @Valid @RequestBody NewCategory newCategory){
        log.info("called method updateCategory");
        return ResponseEntity
                .ok()
                .body(adminService.updateCategoryName(categoryId, newCategory));
    }

    @Operation(summary = "Добавить продукт")
    @PostMapping("/products/createProduct")
    public ResponseEntity<Void> addProduct(
            @Valid @RequestBody NewProduct product){
        log.info("called admin method addProduct");
        adminService.addProduct(product);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Operation(summary = "Обновить продукт")
    @PatchMapping("/products/update/{productId}")
    public ResponseEntity<ProductOut> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductToUpdate newInfo) {
        log.info("called method updateProduct");
        return ResponseEntity
                .ok()
                .body(adminService.updateProduct(productId, newInfo));
    }

    @Operation(summary = "Удалить продукт")
    @DeleteMapping("/products/delete/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long productId
    ){
        log.info("called method deleteProduct");
        adminService.deleteProduct(productId);
        return ResponseEntity
                .ok()
                .build();
    }

    @Operation(summary = "Повысить обычного юзера до админа")
    @PostMapping("/users/promoteToAdmin")
    public ResponseEntity<Void> promoteToAdmin(
            @Valid @RequestBody UserToAdmin user
            ){
        log.info("called method promoteToAdmin");
        adminService.promoteToAdmin(user);
        return ResponseEntity
                .ok()
                .build();
    }


}
