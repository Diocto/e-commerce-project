package kr.hhplus.be.server.interfaces.dto.payment;

import java.util.List;

public record OrderResponseBody(
        Long orderId,
        Long userId,
        List<OrderProductReponse> orderProductList,
        Long totalAmount
) {

}
