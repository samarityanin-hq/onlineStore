package main.store.Controllers;

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

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping("/{title}")
    public ResponseEntity<ProductOut> getProduct(
            @PathVariable String title){

        log.info("called method getProduct");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.getProduct(title));
    }

    @GetMapping("/categories")
    public ResponseEntity<CategoryList> getCategory(){
        log.info("called method getCategories");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.getCategories());
    }

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
