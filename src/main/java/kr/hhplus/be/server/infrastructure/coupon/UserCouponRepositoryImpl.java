package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.IUserCouponRepository;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserCouponRepositoryImpl implements IUserCouponRepository {
    private final UserCouponJpaRepository userCouponJpaRepository;

    public UserCouponRepositoryImpl(UserCouponJpaRepository userCouponJpaRepository) {
        this.userCouponJpaRepository = userCouponJpaRepository;
    }

    @Override
    public Optional<UserCoupon> findById(Long id) {
        return userCouponJpaRepository.findById(id);
    }

    @Override
    public void save(UserCoupon userCoupon) {
        userCouponJpaRepository.save(userCoupon);
    }

    @Override
    public Optional<UserCoupon> findByUserIdAndCoupon(Long userId, Coupon coupon) {
        return userCouponJpaRepository.findByUserIdAndCoupon(userId, coupon);
    }
}
