package main.store.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import main.store.DTO.DTOout.CategoryList;
import main.store.DTO.DTOin.ProductToAdd;
import main.store.DTO.DTOin.UserToAdmin;
import main.store.Services.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Контроллер админа", description = "Доступно только пользователям со статусом админа")
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    @Operation(summary = "Показать доступные категории")
    @GetMapping("/addProduct/getCategories")
    public ResponseEntity<CategoryList> getCategories(){
        log.info("called admin method getCategories");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(adminService.getCategories());
    }

    @Operation(summary = "Добавить продукт")
    @PostMapping("/addProduct/createProduct")
    public ResponseEntity<Void> addProduct(
            @Valid @RequestBody ProductToAdd product){
        log.info("called admin method addProduct");
        adminService.addProduct(product);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @Operation(summary = "Повысить обычного юзера до админа")
    @PostMapping("/promoteToAdmin")
    public ResponseEntity<Void> promoteToAdmin(
            @RequestBody UserToAdmin user
            ){
        log.info("called method promoteToAdmin");
        adminService.promoteToAdmin(user);
        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }


}
