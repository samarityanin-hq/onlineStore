package main.store.RepositoriesTest;

import main.store.DTO.DTOout.ItemOut;
import main.store.Entities.CartItem;
import main.store.Entities.Product;
import main.store.Entities.User;
import main.store.Enums.UserRole;
import main.store.Repositories.CartRepo;
import main.store.Repositories.ProductRepo;
import main.store.Repositories.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class CartRepoTest {

    @ServiceConnection
    private static PostgreSQLContainer<?> sqlContainer =
            new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private TestEntityManager entityManager;

    private List<CartItem> items;
    private User user;

    @BeforeEach
    void setUp(){
        user = new User();
        user.setName("user");
        user.setRole(UserRole.ROLE_USER);

        Product product1 = new Product();
        product1.setPrice(new BigDecimal("200"));
        Product product2 = new Product();
        product2.setPrice(new BigDecimal("1000"));

        CartItem item1 = new CartItem();
        item1.setUser(user);
        item1.setItemQuantity(5);
        item1.setItem(product1);

        CartItem item2 = new CartItem();
        item2.setUser(user);
        item2.setItemQuantity(2);
        item2.setItem(product2);

        items = new ArrayList<>(List.of(item1, item2));
        userRepo.save(user);
        productRepo.save(product1);
        productRepo.save(product2);
        cartRepo.saveAll(items);


        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void checkSetUp(){
        List<CartItem> result = cartRepo.findAll();

        assertNotNull(result);
        assertEquals(items.getFirst().getId(), result.getFirst().getId());
        assertEquals(0, result.getFirst().getPositionCost()
                .compareTo(items.getFirst().getPositionCost()));

        assertEquals(items.get(1).getId(), result.get(1).getId());
        assertEquals(0, result.get(1).getPositionCost()
                .compareTo(items.get(1).getPositionCost()));

        assertTrue(userRepo.existsByName("user"));
        assertNotNull(user.getId());
    }

    @Test
    void findCartByUserId_cartFound(){
        List<ItemOut> result = cartRepo.findDTO(user.getId());

        assertNotNull(result);

        assertEquals(0, result.getFirst().totalPrice()
                .compareTo(items.getFirst().getPositionCost()));
        assertEquals(items.getFirst().getItemQuantity(), result.getFirst().quantity());
        assertEquals(items.getFirst().getItem().getTitle(), result.getFirst().name());

        assertEquals(0, result.get(1).totalPrice()
                .compareTo(items.get(1).getPositionCost()));
        assertEquals(items.get(1).getItemQuantity(), result.get(1).quantity());
        assertEquals(items.get(1).getItem().getTitle(), result.get(1).name());
    }

    @Test
    void findCartByUserId_userWithIdNotExist(){
        List<ItemOut> result = cartRepo.findDTO(-1L);

      assertTrue(result.isEmpty());
    }

    @Test
    void getCartItemByItemIdAndUserId_itemFound(){
        Optional<CartItem> result = cartRepo.getByItem_IdAndUser_Id(items.getFirst().getId(), user.getId());

        assertFalse(result.isEmpty());
        assertEquals(0, result.get().getPositionCost()
                .compareTo(items.getFirst().getPositionCost()));
        assertEquals(items.getFirst().getItemQuantity(), result.get().getItemQuantity());
    }

    @Test
    void getCartItemByItemIdAndUserId_itemNotFound_itemIdNotExist(){
        Optional<CartItem> result = cartRepo.getByItem_IdAndUser_Id(-1L, user.getId());
        assertTrue(result.isEmpty());
    }

    @Test
    void getCartItemByItemIdAndUserId_itemNotFound_userWithIdNotExist(){
        Optional<CartItem> result = cartRepo.getByItem_IdAndUser_Id(items.getFirst().getId(), -1L);
        assertTrue(result.isEmpty());
    }

    @Test
    void deleteByItemIdAndUserId_itemDeleted(){
        cartRepo.deleteByIdAndUserId(items.getFirst().getId(), user.getId());

        Optional<CartItem> result = cartRepo.getByItem_IdAndUser_Id(items.getFirst().getId(), user.getId());

        assertTrue(result.isEmpty());
    }

    @Test
    void deleteAllItemByUserId_cartCleared(){
        cartRepo.deleteAllByUser_Id(user.getId());
        Optional<CartItem> result1 = cartRepo.getByItem_IdAndUser_Id(items.getFirst().getId(), user.getId());
        Optional<CartItem> result2 = cartRepo.getByItem_IdAndUser_Id(items.get(1).getId(), user.getId());

        List<CartItem> deletedItems = cartRepo.findCartItemsByUserId(user.getId());

        assertTrue(result1.isEmpty());
        assertTrue(result2.isEmpty());
        assertTrue(deletedItems.isEmpty());

    }




}
