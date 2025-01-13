package kr.hhplus.be.server.interfaces.api.payment;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderUseCase;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderUseCase orderUseCase;

    public OrderController(OrderUseCase orderUseCase) {
        this.orderUseCase = orderUseCase;
    }

    public static class Request{
        public record OrderBody(
                Long userId,
                List<Request.OrderProduct> orderProductRequests,
                Long userCouponId
        ) {

        }
        public record OrderProduct(
                Long productId,
                Long quantity
        ) {
        }
    }

    public static class Response{
        public record Order(
                Long orderId,
                Long userId,
                List<Response.OrderProduct> orderProductList,
                Long userCouponId,
                Long totalAmount,
                Long discountAmount
        ) {

        }

        public record OrderProduct(
                Long productId,
                Long price,
                Long quantity
        ) {
        }

    }

    @PostMapping()
    public ResponseEntity<Response.Order> createOrder(@RequestBody Request.OrderBody requestBody) {
        List<Pair<Long, Long>> productIdQuantityPair = new ArrayList<>();
        for (Request.OrderProduct orderProductRequest : requestBody.orderProductRequests) {
            productIdQuantityPair.add(Pair.of(orderProductRequest.productId, orderProductRequest.quantity));
        }
        Order order = orderUseCase.createOrder(requestBody.userId, productIdQuantityPair, requestBody.userCouponId);
        return ResponseEntity.ok(new Response.Order(
                order.getId(),
                order.getUserId(),
                order.getOrderProducts().stream().map(orderProduct -> new Response.OrderProduct(
                        orderProduct.getProductId(),
                        orderProduct.getPrice(),
                        orderProduct.getQuantity()
                )).toList(),
                order.getUserCouponId(),
                order.getOrderProducts().stream().mapToLong(orderProduct -> orderProduct.getPrice() * orderProduct.getQuantity()).sum(),
                order.getDiscountAmount()
        ));
    }
}
