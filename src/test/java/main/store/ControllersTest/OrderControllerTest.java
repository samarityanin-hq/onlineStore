package main.store.ControllersTest;

import main.store.Controllers.OrderController;
import main.store.Exceptions.CustomExceptions.EmptyCartException;
import main.store.Exceptions.CustomExceptions.InvalidPaymentAmountException;
import main.store.Exceptions.CustomExceptions.OrderAlreadyPaidException;
import main.store.Exceptions.CustomExceptions.ProductOutOfStockException;
import main.store.DTO.Request.PaymentIn;
import main.store.DTO.Response.FullOrderOut;
import main.store.DTO.Response.OrderItemOut;
import main.store.DTO.Response.PaymentResponse;
import main.store.Enums.Status;
import main.store.Security.CustomUserDetails;
import main.store.Services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(OrderController.class)
public class OrderControllerTest extends AbstractWebTest {
    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    private String json;

    @BeforeEach
    void setUp(){
        setUpConfig();
        json = """
                {
                "amount": "999.99"
                }
                """;

    }

    @Test
    void createOrder_createdSuccessfully() throws Exception {

        OrderItemOut mockItem = new OrderItemOut("Item_X", 2, new BigDecimal("399.99"));

        FullOrderOut mockResponse = new FullOrderOut(
                "user@email.com",
                "user",
                Status.CREATED,
                new BigDecimal("399.99"),
                List.of(mockItem)
        );


        when(orderService.createOrder(userDetails))
                .thenReturn(mockResponse);

        System.out.println(mockResponse);

        mockMvc.perform(post("/orders/createOrder")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("user@email.com"))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.price").value("399.99"));

        verify(orderService).createOrder(any());

    }

    @Test
    void createOrder_emptyCart() throws Exception {
        when(orderService.createOrder(userDetails))
                .thenThrow(new EmptyCartException("user@email.com", "user"));

        mockMvc.perform(post("/orders/createOrder")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message")
                        .value("User user, email: user@email.com, has empty cart"));

        verify(orderService, times(1)).createOrder(any());

    }

    @Test
    void createOrder_productOutOfStock() throws Exception {
        when(orderService.createOrder(any(CustomUserDetails.class)))
                .thenThrow(new ProductOutOfStockException("product_X", 1L));

        mockMvc.perform(post("/orders/createOrder")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message")
                        .value("Product name: product_X, id: 1, is out of stock"));

        verify(orderService, times(1)).createOrder(any());

    }

    @Test
    void pay_paySuccessfully() throws Exception {
        PaymentResponse response = new PaymentResponse(
                1L,
                "user@email.com",
                new BigDecimal("999.99"),
                LocalDateTime.now());

        when(orderService.pay(any(PaymentIn.class), eq(1L), any(CustomUserDetails.class)))
                .thenReturn(response);

        mockMvc.perform(post("/orders/1/pay")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1))
                .andExpect(jsonPath("$.userEmail").value("user@email.com"))
                .andExpect(jsonPath("$.orderCost").value("999.99"));

        verify(orderService).pay(any(), any(), any());

    }

    @Test
    void pay_orderAlreadyPaid() throws Exception {

        when(orderService.pay(any(PaymentIn.class), eq(1L), any(CustomUserDetails.class)))
                .thenThrow(new OrderAlreadyPaidException(1L));

        mockMvc.perform(post("/orders/1/pay")
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value("409"))
                .andExpect(jsonPath("$.message")
                        .value("Order 1, is already paid"));

        verify(orderService, times(1)).pay(any(), any(), any());

    }

    @Test
    void pay_invalidPaymentAmount() throws Exception {

        when(orderService.pay(any(PaymentIn.class), eq(1L), any(CustomUserDetails.class)))
                .thenThrow(new InvalidPaymentAmountException(1L, new BigDecimal("999.99"), new BigDecimal("500")));

        mockMvc.perform(post("/orders/1/pay")
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value("409"))
                .andExpect(jsonPath("$.message")
                        .value("For order 1 expected: 999.99, get 500"));

        verify(orderService, times(1)).pay(any(), any(), any());
    }


}
