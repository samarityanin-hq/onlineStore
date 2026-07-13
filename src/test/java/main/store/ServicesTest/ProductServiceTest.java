package main.store.ServicesTest;

import jakarta.persistence.EntityNotFoundException;
import main.store.DTO.DTOout.CategoryList;
import main.store.DTO.DTOout.CategoryOut;
import main.store.DTO.DTOout.ProductOut;
import main.store.Entities.Category;
import main.store.Repositories.CategoryRepo;
import main.store.Repositories.ProductRepo;
import main.store.Services.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepo productRepo;
    @Mock
    private CategoryRepo categoryRepo;

    @Test
    void getProduct_correctTitle(){
        ProductOut productOut = new ProductOut("Phone", new BigDecimal("999.99"), 20);

        when(productRepo.findProductDTO("Phone"))
                .thenReturn(Optional.of(productOut));

        ProductOut result = productService.getProduct("Phone");

        assertNotNull(result);
        assertEquals("Phone", result.title());
        assertEquals(new BigDecimal("999.99"), result.price());
        assertEquals(20, result.quantity());
    }

    @Test
    void getProduct_invalidTitle_throwsException(){
        when(productRepo.findProductDTO("invalidTitle"))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> productService.getProduct("invalidTitle"));
    }

    @Test
    void getProductCatalog(){
        ProductOut product1 = new ProductOut("Phone", new BigDecimal("999.99"), 20);
        ProductOut product2 = new ProductOut("Laptop", new BigDecimal("1499.99"), 10);

        Page<ProductOut> page = new PageImpl<>(List.of(product1, product2));

        when(productRepo.getProductList(any(Pageable.class)))
                .thenReturn(page);

        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductOut> result = productService.getProductsCatalog(pageable);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("Phone", result.getContent().getFirst().title());
        assertEquals("Laptop", result.getContent().get(1).title());

    }

    @Test
    void getCategories(){
        CategoryOut category1 = new CategoryOut(0L, "Phones");
        CategoryOut category2 = new CategoryOut( 1L, "Earphones");

        when(categoryRepo.getAllCategories())
                .thenReturn(List.of(category1, category2));

        CategoryList result = productService.getCategories();

        assertNotNull(result);
        assertEquals(2, result.categories().size());
        assertEquals("Phones", result.categories().get(0).name());
        assertEquals("Earphones", result.categories().get(1).name());
    }

    @Test
    void sortByCategoryName_correctCategoryName(){
        ProductOut product1 = new ProductOut("Phone1", new BigDecimal("999.99"), 20);
        ProductOut product2 = new ProductOut("Phone2", new BigDecimal("1499.99"), 10);

        Page<ProductOut> page = new PageImpl<>(List.of(product1, product2));

        when(productRepo.findByCategoryName(eq("Phones"),any(Pageable.class)))
                .thenReturn(page);

        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductOut> result = productService.sortByCategory("Phones",pageable);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("Phone1", result.getContent().getFirst().title());
        assertEquals("Phone2", result.getContent().get(1).title());
    }

    @Test
    void sortByCategoryName_invalidCategoryName(){
        Pageable pageable = PageRequest.of(0, 10);
        when(productRepo.findByCategoryName(eq("invalidCategory"),any(Pageable.class)))
                .thenReturn(Page.empty());

        Page<ProductOut> result = productService.sortByCategory("invalidCategory",pageable);

        assertNotNull(result);
        assertTrue(result.isEmpty());

    }



}
