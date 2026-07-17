package main.store.ServicesTest;

import main.store.CustomExceptions.EmptyCartException;
import main.store.CustomExceptions.InvalidPaymentAmountException;
import main.store.CustomExceptions.OrderAlreadyPaidException;
import main.store.CustomExceptions.ProductOutOfStockException;
import main.store.DTO.DTOin.PaymentIn;
import main.store.DTO.DTOout.FullOrderOut;
import main.store.DTO.DTOout.PaymentResponse;
import main.store.Entities.CartItem;
import main.store.Entities.Order;
import main.store.Entities.Product;
import main.store.Entities.User;
import main.store.Enums.Status;
import main.store.Repositories.CartRepo;
import main.store.Repositories.OrderItemRepo;
import main.store.Repositories.OrderRepo;
import main.store.Repositories.UserRepo;
import main.store.Security.CustomUserDetails;
import main.store.Services.OrderService;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @InjectMocks
    private OrderService orderService;

    @Mock
    private CartRepo cartRepo;
    @Mock
    private UserRepo userRepo;
    @Mock
    private OrderRepo orderRepo;
    @Mock
    private OrderItemRepo orderItemRepo;

    private CustomUserDetails userDetails;
    private User user;
    @BeforeEach
    void setUp(){
        userDetails = mock(CustomUserDetails.class);
        when(userDetails.getId()).thenReturn(1L);
        lenient().when(userDetails.getRealName()).thenReturn("user");
        lenient().when(userDetails.getUsername()).thenReturn("user@gmail.com");

        user = new User();
        user.setId(1L);

    }

    @Test
    void createOrder_createdSuccessfully(){
        List<CartItem> cartItems = getCartItems();

        when(cartRepo.findCartItemsByUserId(userDetails.getId())).thenReturn(cartItems);
        when(userRepo.getReferenceById(userDetails.getId())).thenReturn(user);

        FullOrderOut result = orderService.createOrder(userDetails);

        assertNotNull(result);
        assertEquals("user", userDetails.getRealName());
        assertEquals("user@gmail.com",userDetails.getUsername());
        assertEquals(Status.CREATED, result.status());
        assertEquals(new BigDecimal("200"), result.price());
        assertEquals(2, result.orderItems().size());
        assertEquals(2, result.orderItems().get(1).quantity());

        verify(cartRepo).findCartItemsByUserId(any());
        verify(userRepo).getReferenceById(any());

    }

    private @NonNull List<CartItem> getCartItems() {
        Product product1 = new Product();
        product1.setTitle("product1");
        product1.setPrice(new BigDecimal("100"));
        product1.setStorageQuantity(20);
        Product product2 = new Product();
        product2.setTitle("product2");
        product2.setPrice(new BigDecimal("50"));
        product2.setStorageQuantity(20);

        CartItem cartItem1 = new CartItem(user, product1, 1);
        CartItem cartItem2 = new CartItem(user, product2, 2);

        List<CartItem> cartItems = List.of(cartItem1, cartItem2);
        return cartItems;
    }

    @Test
    void createOrder_productOutOfStock(){
        Product product = new Product();
        product.setId(1L);
        product.setStorageQuantity(0);
        CartItem cartItem = new CartItem(user, product, 1);

        when(cartRepo.findCartItemsByUserId(userDetails.getId())).thenReturn(List.of(cartItem));
        when(userRepo.getReferenceById(userDetails.getId())).thenReturn(user);

        assertThrows(ProductOutOfStockException.class,
                () -> orderService.createOrder(userDetails));

        verify(cartRepo).findCartItemsByUserId(any());
        verify(userRepo).getReferenceById(any());
    }

    @Test
    void createOrder_emptyCart(){
        when(cartRepo.findCartItemsByUserId(userDetails.getId())).thenReturn(Collections.emptyList());
        assertThrows(EmptyCartException.class,
                () -> orderService.createOrder(userDetails));

        verify(cartRepo, times(1)).findCartItemsByUserId(any());
        verify(userRepo, never()).getReferenceById(any());

    }

    @Test
    void pay_payedSuccessfully(){
        Order order = new Order();
        order.setId(1L);
        order.setStatus(Status.CREATED);
        order.setTotalPrice(new BigDecimal("100"));
        order.setUser(user);
        PaymentIn payment = new PaymentIn(new BigDecimal("100"));

        when(orderRepo.findByUserIdAndOrderId(userDetails.getId(), 1L)).thenReturn(order);

        PaymentResponse result = orderService.pay(payment, 1L, userDetails);

        assertNotNull(result);
        assertEquals(1L, result.orderId());
        assertEquals("user@gmail.com", result.userEmail());
        assertEquals(new BigDecimal("100") ,result.orderCost());
        assertEquals(Status.PAID, order.getStatus());
        assertNotNull(result.payTime());

        verify(orderRepo).findByUserIdAndOrderId(any(), any());
        verify(orderRepo).save(any());

    }

    @Test
    void pay_orderAlreadyPaid(){
        Order order = new Order();
        order.setStatus(Status.PAID);
        PaymentIn payment = new PaymentIn(new BigDecimal("100"));

        when(orderRepo.findByUserIdAndOrderId(userDetails.getId(), 1L)).thenReturn(order);

        assertThrows(OrderAlreadyPaidException.class,
                () -> orderService.pay(payment, 1L, userDetails));

        verify(orderRepo, times(1)).findByUserIdAndOrderId(any(), any());
        verify(orderRepo, never()).save(any());
    }

    @Test
    void pay_invalidPaymentAmount(){
        Order order = new Order();
        order.setStatus(Status.CREATED);
        order.setTotalPrice(new BigDecimal("100"));
        PaymentIn payment = new PaymentIn(new BigDecimal("50"));

        when(orderRepo.findByUserIdAndOrderId(userDetails.getId(), 1L)).thenReturn(order);

        assertThrows(InvalidPaymentAmountException.class,
                () -> orderService.pay(payment, 1L, userDetails));

        verify(orderRepo, times(1)).findByUserIdAndOrderId(any(), any());
        verify(orderRepo, never()).save(any());
    }
}
