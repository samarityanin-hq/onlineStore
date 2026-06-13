package main.store.Controllers;

import main.store.DTOs.ProductOut;
import main.store.Services.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/{title}")
    public ResponseEntity<ProductOut> getProduct(
            @PathVariable String title){

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.getProduct(title));
    }

    @GetMapping("/catalog")
    public ResponseEntity<List<ProductOut>> getCatalog(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(productService.getProductsCatalog());
    }




}
