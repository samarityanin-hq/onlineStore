package main.store.ControllersTest;

import main.store.Controllers.CartController;
import main.store.Services.CartService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CartController.class)
public class CartControllerTest extends AbstractWebTest {

    @MockitoBean
    private CartService cartService;

    @BeforeEach
    void setUp(){
        setUpConfig();
    }
    @AfterEach
    void clearAuthenticationContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void addCartItem_addedSuccessfully() throws Exception{
        mockMvc.perform(post("/cart/add")
                        .param("productId", "1")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk());

        verify(cartService).addToCart(eq(1L), any());
    }

    @Test
    void addCartItem_invalidParam() throws Exception {
        mockMvc.perform(post("/cart/add")
                .param("productId", "invalidParam")
                .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message")
                        .value("Param productId must be number"));

        verify(cartService, never()).addToCart(eq(1L), any());
    }

    @Test
    void addCartItem_unauthorizedUser() throws Exception {
        mockMvc.perform(post("/cart/add")
                        .param("productId", "1")
                        .header("Authorization", "Bearer invalid-access-token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Required authorization"));

        verify(cartService, never()).addToCart(eq(1L), any());
    }

}
