package kr.hhplus.be.server.domain.order;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderUseCase {
    private final OrderService orderService;

    public OrderUseCase(OrderService orderService) {
        this.orderService = orderService;
    }

    public Order createOrder(Long userId, List<Pair<Long, Long>> productIdQuantityPair, Long userCouponId) {
        return orderService.createOrder(
                OrderService.Command.CreateOrderCommand.builder()
                        .userId(userId)
                        .products(productIdQuantityPair.stream()
                                .map(pair -> OrderService.Command.ProductCommand.builder()
                                        .id(pair.getFirst())
                                        .quantity(pair.getSecond())
                                        .build())
                                .toList())
                        .build()
        );
    }
}
