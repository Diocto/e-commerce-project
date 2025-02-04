package kr.hhplus.be.server.domain.coupon;

public record CouponCreateRequest(
        Long id,
        Long userId,
        String status,
        Long createdUserCouponId
) {
}
