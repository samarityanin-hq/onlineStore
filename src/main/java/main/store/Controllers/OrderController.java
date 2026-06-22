package main.store.Controllers;

import main.store.DTOs.OrderOut;
import main.store.Entities.CustomUserDetails;
import main.store.Services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController{
    private final OrderService orderService;
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/createOrder")
    public ResponseEntity<OrderOut> createOrder(
            @AuthenticationPrincipal CustomUserDetails userDetails
            ){
        log.info("called method createOrder");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.createOrder(userDetails));

    }
}
