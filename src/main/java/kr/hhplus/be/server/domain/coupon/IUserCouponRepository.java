package kr.hhplus.be.server.domain.coupon;

import java.util.Optional;

public interface IUserCouponRepository {
    Optional<UserCoupon> findById(Long id);
    void save(UserCoupon userCoupon);

    Optional<UserCoupon> findByUserIdAndCoupon(Long userId, Coupon coupon);
}
