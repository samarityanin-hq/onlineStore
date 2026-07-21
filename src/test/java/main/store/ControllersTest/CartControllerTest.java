package main.store.ControllersTest;

import main.store.Controllers.CartController;
import main.store.Security.CustomUserDetails;
import main.store.Security.SecurityConfig;
import main.store.Services.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;


@WebMvcTest(CartController.class)
@Import(SecurityConfig.class)
public class CartControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartService cartService;

    @Test
    void addCartItem_addedSuccessfully() throws Exception{

        mockMvc.perform(post("/cart/add")
                        .param("productId", "1")
                        .with(user("user").roles("USER")))
                .andExpect(status().isOk());

        verify(cartService).addToCart(eq(1L), any());
    }

    @Test
    void addCartItem_invalidParam() throws Exception {
        mockMvc.perform(post("/cart/add")
                .param("productId", "invalidParam")
                        .with(user("user").roles("USER")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.message")
                        .value("Param productId must be number"));

        verify(cartService, never()).addToCart(eq(1L), any());
    }

    @Test
    void addCartItem_unauthorizedUser() throws Exception {
        mockMvc.perform(post("/cart/add")
                        .param("productId", "1"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Required authorization"));

        verify(cartService, never()).addToCart(eq(1L), any());
    }

}
