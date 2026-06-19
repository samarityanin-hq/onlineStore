package main.store.Controllers;

import jakarta.validation.Valid;
import main.store.DTOs.CategoryList;
import main.store.DTOs.ProductOut;
import main.store.Services.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/addProduct/getCategories")
    public ResponseEntity<CategoryList> getCategories(){
        log.info("called admin method getCategories");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(adminService.getCategories());
    }

    @PostMapping("/addProduct/createProduct")
    public ResponseEntity<Void> addProduct(
            @Valid @RequestBody ProductOut productOut){
        log.info("called admin method addProduct");
        adminService.addProduct(productOut);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();

    }
}
