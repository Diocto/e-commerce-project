package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCouponJpaRepository extends JpaRepository<UserCoupon, Long> {
    @Query("SELECT uc FROM UserCoupon uc WHERE uc.userId = :userId AND uc.coupon = :coupon")
    Optional<UserCoupon> findByUserIdAndCoupon(Long userId, Coupon coupon);
}
