package main.store.ControllersTest;

import jakarta.persistence.EntityNotFoundException;
import main.store.Controllers.ProductController;
import main.store.DTO.Response.ProductOut;
import main.store.Services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.math.BigDecimal;


@WebMvcTest(value = ProductController.class)
public class ProductControllerTest extends AbstractWebTests{

    @MockitoBean
    private ProductService productService;

    @BeforeEach
    void setUp(){
        setUpConfig();
    }

    @Test
    void getProduct_validTitle() throws Exception {
        ProductOut product = new ProductOut("Phone", new BigDecimal("299.99"), 20);

        when(productService.getProduct("Phone")).thenReturn(product);

        mockMvc.perform(get("/products/Phone"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Phone"));

        verify(productService).getProduct(any());
    }

    @Test
    void getProduct_invalidTitle() throws Exception {
        when(productService.getProduct("invalid"))
                .thenThrow(new EntityNotFoundException("Product with title invalid doesnt exist"));

        mockMvc.perform(get("/products/invalid"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Product with title invalid doesnt exist"));

        verify(productService, times(1)).getProduct(any());
    }


}
