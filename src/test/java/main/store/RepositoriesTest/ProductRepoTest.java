package main.store.RepositoriesTest;

import main.store.DTO.DTOout.ProductOut;
import main.store.Entities.Category;
import main.store.Entities.Product;
import main.store.Repositories.CategoryRepo;
import main.store.Repositories.ProductRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class ProductRepoTest {

    @ServiceConnection
    private static PostgreSQLContainer<?> sqlContainer =
            new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private TestEntityManager entityManager;

    private List<Product> products;

    @BeforeEach
    void setUp(){
        Category category = new Category();
        category.setName("testCategory");

        Product product = new Product();
        product.setTitle("Product");
        product.setPrice(new BigDecimal("1000"));
        product.setStorageQuantity(20);
        product.setCategory(category);

        Product product1 = new Product();
        product1.setTitle("Product1");
        product1.setPrice(new BigDecimal("500"));
        product1.setStorageQuantity(10);
        product1.setCategory(category);

        products = new ArrayList<>(List.of(product, product1));
        productRepo.saveAll(products);
        categoryRepo.save(category);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void checkSetUp(){
        List<Product> result = productRepo.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(products.getFirst().getId(), result.getFirst().getId());
        assertEquals(products.get(1).getId(), result.get(1).getId());
    }

    @Test
    void findProductDtoByTitle_DtoFound(){
        Optional<ProductOut> result = productRepo.findProductDTO("Product1");

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(new BigDecimal("500.00"), result.get().price());
    }

    @Test
    void findProductDtoByTitle_DtoNotFound(){
        Optional<ProductOut> result = productRepo.findProductDTO("nonExistingProduct");

        assertTrue(result.isEmpty());
    }

    @Test
    void getProductList_ListFound(){
        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by("price").ascending());

        Page<ProductOut> result = productRepo.getProductList(pageRequest);

        assertNotNull(result);
        assertEquals(1, result.getTotalPages());
        assertEquals(2,result.getContent().size());
        assertFalse(result.hasNext());

        List<ProductOut> contentPage = result.getContent();
        assertEquals(new BigDecimal("500.00"), contentPage.getFirst().price());
        assertEquals(new BigDecimal("1000.00"), contentPage.get(1).price());
    }

    @Test
    void getProductList_emptyNextPage(){
        PageRequest pageRequest = PageRequest.of(1, 2);

        Page<ProductOut> result = productRepo.getProductList(pageRequest);

        assertNotNull(result);
        assertEquals(0, result.getContent().size());
        assertEquals(1, result.getTotalPages());
        assertFalse(result.hasNext());
        assertTrue(result.hasPrevious());
    }

    @Test
    void findByCategoryName_categoryFound(){
        PageRequest pageRequest = PageRequest.of(0, 2);

        Page<ProductOut> result = productRepo.findByCategoryName("testCategory", pageRequest);

        assertNotNull(result);
        assertEquals(1, result.getTotalPages());
        assertEquals(2, result.getContent().size());
        assertFalse(result.getContent().isEmpty());
    }

    @Test
    void findByCategoryName_categoryNotFound_emptyPage(){
        PageRequest pageRequest = PageRequest.of(0, 2);

        Page<ProductOut> result = productRepo.findByCategoryName("invalidCategory", pageRequest);

        assertNotNull(result);
        assertEquals(0, result.getTotalPages());
        assertEquals(0, result.getTotalElements());

        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getContent().size());
    }


}
