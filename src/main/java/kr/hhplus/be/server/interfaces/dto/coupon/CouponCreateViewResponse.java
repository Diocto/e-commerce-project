package kr.hhplus.be.server.interfaces.dto.coupon;

import java.util.Optional;

public record CouponCreateViewResponse (
        String couponCreateRequestStatus,
        Optional<Long> createdUserCouponId
){
}
