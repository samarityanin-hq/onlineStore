package main.store.Controllers;

import main.store.DTOs.ProductOut;
import main.store.Services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    public ProductController(ProductService productService) {
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

    @GetMapping("/catalog")
    public ResponseEntity<List<ProductOut>> getCatalog(){
        log.info("called method getCatalog");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.getProductsCatalog());
    }




}
