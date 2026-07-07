package main.store.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import main.store.DTO.DTOout.CategoryList;
import main.store.DTO.DTOout.ProductOut;
import main.store.Services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Контроллер продуктов")
@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    @Operation(summary = "Получить товар по имени")
    @GetMapping("/{title}")
    public ResponseEntity<ProductOut> getProduct(
            @PathVariable String title){

        log.info("called method getProduct");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.getProduct(title));
    }

    @Operation(summary = "Получить список категорий")
    @GetMapping("/categories")
    public ResponseEntity<CategoryList> getCategory(){
        log.info("called method getCategories");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.getCategories());
    }

    @Operation(summary = "Получить все товары из одной категории")
    @GetMapping("/catalog/{categoryName}")
    public ResponseEntity<Page<ProductOut>> getProductsByCategory(
            @PathVariable String categoryName,
            Pageable pageable
    ){
        log.info("called method getProductsByCategory");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.getProductByCategory(categoryName, pageable));
    }

    @Operation(summary = "Получить каталог товаров")
    @GetMapping("/catalog")
    public ResponseEntity<Page<ProductOut>> getCatalog(
            Pageable pageable
    ){
        log.info("called method getCatalog");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.getProductsCatalog(pageable));
    }






}
