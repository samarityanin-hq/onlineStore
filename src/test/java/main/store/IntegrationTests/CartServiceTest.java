package main.store.IntegrationTests;

import jakarta.persistence.EntityNotFoundException;
import main.store.DTO.Response.CartItemsOut;
import main.store.Entities.CartItem;
import main.store.Entities.Product;
import main.store.Entities.User;
import main.store.Repositories.CartRepo;
import main.store.Repositories.ProductRepo;
import main.store.Repositories.UserRepo;
import main.store.Security.CustomUserDetails;
import main.store.Services.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class CartServiceTest{

    @Container
    @ServiceConnection
    private static PostgreSQLContainer<?> sqlContainer =
            new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private CartService cartService;
    @Autowired
    protected CartRepo cartRepo;
    @Autowired
    protected UserRepo userRepo;
    @Autowired
    protected ProductRepo productRepo;

    private User user;
    private CustomUserDetails userDetails;
    private Product product1;

    @BeforeEach
    void setUp(){
        product1 = new Product();
        product1.setPrice(new BigDecimal("200"));
        product1.setTitle("testProduct");
        productRepo.save(product1);

        user = new User();
        userRepo.save(user);

        userDetails = new CustomUserDetails(user,
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

    }

    @Test
    void addToCart_zeroItems_totalOneItem(){
        cartService.addToCart(product1.getId(), userDetails);

        Optional<CartItem> resultItem = cartRepo.getByItem_IdAndUser_Id(product1.getId(), userDetails.getId());

        assertTrue(resultItem.isPresent());
        assertEquals(1, resultItem.get().getItemQuantity());
        assertEquals(0, resultItem.get().getPositionCost()
                .compareTo(product1.getPrice()));
        assertEquals(user.getId(), resultItem.get().getUser().getId());
    }

    @Test
    void addToCart_oneItem_totalTwoItems(){
        CartItem item = new CartItem(user, product1, 1);
        cartRepo.save(item);

        cartService.addToCart(product1.getId(), userDetails);

        Optional<CartItem> resultItem = cartRepo.getByItem_IdAndUser_Id(product1.getId(), userDetails.getId());

        assertTrue(resultItem.isPresent());
        assertEquals(2, resultItem.get().getItemQuantity());
        assertEquals(0, new BigDecimal("400")
                .compareTo(resultItem.get().getPositionCost()));
        assertEquals(user.getId(), resultItem.get().getUser().getId());
    }

    @Test
    void addToCart_incorrectProductId(){
        assertThrows(EntityNotFoundException.class, () ->cartService.addToCart(-1L, userDetails));
    }

    @Test
    void clear_itemCleared(){
        CartItemsOut result = cartService.clear(userDetails);

        assertTrue(result.items().isEmpty());
        assertEquals(0, new BigDecimal("0")
                .compareTo(result.totalPrice()));
    }

    @Test
    void decrementCartPosition_twoItemsToOne(){
        CartItem item = new CartItem(user, product1, 2);
        cartRepo.save(item);

        CartItemsOut result = cartService.decrementCartPosition(product1.getId(), userDetails);


        assertFalse(result.items().isEmpty());
        assertEquals(1, result.items().getFirst().quantity());
        assertEquals(0, new BigDecimal("200")
                .compareTo(result.items().getFirst().totalPrice()));
    }

    @Test
    void decrementCartPosition_oneToZero(){
        CartItem item = new CartItem(user, product1, 1);
        cartRepo.save(item);

        CartItemsOut result = cartService.decrementCartPosition(product1.getId(), userDetails);

        assertTrue(result.items().isEmpty());
        assertEquals(0, new BigDecimal("0")
                .compareTo(result.totalPrice()));

    }

    @Test
    void deleteCartPosition_twoToZero(){
        CartItem item = new CartItem(user, product1, 2);
        cartRepo.save(item);

        CartItemsOut result = cartService.deleteCartPosition(item.getId(), userDetails);

        assertTrue(result.items().isEmpty());
        assertEquals(0, new BigDecimal("0")
                .compareTo(result.totalPrice()));
    }

    @Test
    void deleteCartPosition_incorrectItemId(){
        Exception ex = assertThrows(EntityNotFoundException.class,
                ()->cartService.deleteCartPosition(-1L, userDetails));
        assertEquals("INVALID ID: item doesnt exist", ex.getMessage());

    }



}
