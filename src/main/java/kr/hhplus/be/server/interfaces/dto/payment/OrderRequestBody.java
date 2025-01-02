package kr.hhplus.be.server.interfaces.dto.payment;

import java.util.List;

public record OrderRequestBody(
        Long userId,
        List<OrderProductRequest> orderProductRequests,
        Long userCouponId,
        Long totalAmount
) {

}
