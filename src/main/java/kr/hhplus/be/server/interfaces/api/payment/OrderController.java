package kr.hhplus.be.server.interfaces.api.payment;

import kr.hhplus.be.server.interfaces.dto.payment.OrderProductReponse;
import kr.hhplus.be.server.interfaces.dto.payment.OrderRequestBody;
import kr.hhplus.be.server.interfaces.dto.payment.OrderResponseBody;
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
    @PostMapping()
    public ResponseEntity<OrderResponseBody> createOrder(@RequestBody OrderRequestBody requestBody) {
        OrderProductReponse orderProductReponse = new OrderProductReponse(1L, 1000L, 1L);
        OrderProductReponse orderProductReponse2 = new OrderProductReponse(2L, 1000L, 1L);
        List<OrderProductReponse> orderProductReponses = new ArrayList<>();
        orderProductReponses.add(orderProductReponse);
        orderProductReponses.add(orderProductReponse2);
        OrderResponseBody orderResponseBody = new OrderResponseBody(1L, 1L, orderProductReponses, 1L, 2000L);
        return ResponseEntity.ok(orderResponseBody);
    }
}
