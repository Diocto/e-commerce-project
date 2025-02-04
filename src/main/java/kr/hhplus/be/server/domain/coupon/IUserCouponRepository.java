package kr.hhplus.be.server.domain.coupon;

import java.util.Optional;

public interface IUserCouponRepository {
    Optional<UserCoupon> findById(Long id);
    void save(UserCoupon userCoupon);

    Optional<UserCoupon> findByUserIdAndCouponId(Long userId, Long couponId);
}
