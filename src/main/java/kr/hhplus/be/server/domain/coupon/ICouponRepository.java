package kr.hhplus.be.server.domain.coupon;

import java.util.Optional;

public interface ICouponRepository {
    Optional<Coupon> findById(Long id);
    void save(Coupon coupon);
    Optional<Coupon> getByIdWithLock(Long id);
}
