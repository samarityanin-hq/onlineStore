package main.store.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import main.store.DTO.DTOout.FullOrderOut;
import main.store.DTO.DTOin.PaymentIn;
import main.store.DTO.DTOout.PaymentResponse;
import main.store.DTO.DTOout.OrderOut;
import main.store.Security.CustomUserDetails;
import main.store.Services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Контроллер заказов", description = "Управление заказами(только для аутентифицированных юзеров)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController{

    private final OrderService orderService;

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Operation(summary = "Создать заказ")
    @PostMapping("/createOrder")
    public ResponseEntity<FullOrderOut> createOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails
            ){
        log.info("called method createOrder");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.createOrder(userDetails));
    }

    @Operation(summary = "Оплатить(условно) заказ")
    @PostMapping("/{orderId}/pay")
    public ResponseEntity<PaymentResponse> pay(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody PaymentIn payment,
            @PathVariable long orderId){
        log.info("called method pay");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.pay(payment, orderId,userDetails));
    }

    @Operation(summary = "Получить список заказов пользователя")
    @GetMapping("/orderList")
    public ResponseEntity<List<OrderOut>> getOrders(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        log.info("called method getOrders");
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.getOrders(userDetails));
    }


}
