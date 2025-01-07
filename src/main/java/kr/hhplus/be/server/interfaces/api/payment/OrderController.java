package kr.hhplus.be.server.interfaces.api.payment;

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
    public static class Request{
        public record OrderBody(
                Long userId,
                List<Request.OrderProduct> orderProductRequests,
                Long userCouponId,
                Long totalAmount
        ) {

        }
        public record OrderProduct(
                Long productId,
                Long price,
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
                Long totalAmount
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
        Response.OrderProduct orderProductReponse = new Response.OrderProduct(1L, 1000L, 1L);
        Response.OrderProduct orderProductReponse2 = new Response.OrderProduct(2L, 1000L, 1L);
        List<Response.OrderProduct> orderProductReponses = new ArrayList<>();
        orderProductReponses.add(orderProductReponse);
        orderProductReponses.add(orderProductReponse2);
        Response.Order orderResponseBody = new Response.Order(1L, 1L, orderProductReponses, 1L, 2000L);
        return ResponseEntity.ok(orderResponseBody);
    }
}
