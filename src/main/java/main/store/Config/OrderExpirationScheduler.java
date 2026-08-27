package main.store.Config;

import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.store.Entities.Order;
import main.store.Enums.Status;
import main.store.Repositories.OrderRepo;
import main.store.Services.OrderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderExpirationScheduler {
    private final OrderRepo orderRepo;
    private final OrderService orderService;

    @Scheduled(fixedRate = 60_000)
    public void expireStaleOrder(){
        List<Order> expired = orderRepo.findExpiredOrders(Status.CREATED, LocalDateTime.now());

        for (Order order: expired){
            try {
                orderService.cancelExpiredOrder(order.getId());
            } catch (OptimisticLockException e) {
                log.warn(e.getMessage());
            }
        }
    }
}
