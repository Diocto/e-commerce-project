package kr.hhplus.be.server.interfaces.dto.coupon;

public record CouponCreateRequest (
        Long userId,
        Long couponId
){
}
