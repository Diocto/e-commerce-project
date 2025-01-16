package kr.hhplus.be.server.domain.coupon;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.interfaces.api.coupon.CouponController;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CouponService {
    private final ICouponRepository couponRepository;
    private final IUserCouponRepository userCouponRepository;

    public CouponService(ICouponRepository couponRepository, IUserCouponRepository userCouponRepository) {
        this.couponRepository = couponRepository;
        this.userCouponRepository = userCouponRepository;
    }

    public Optional<UserCoupon> getUserCoupon(Long id){
        return userCouponRepository.findById(id);
    }

    public Optional<Coupon> getCoupon(Long id){
        return couponRepository.findById(id);
    }

    @Transactional
    public UserCoupon createLimitedCoupon(Long userId, Long couponId) {
        Coupon coupon = couponRepository.getByIdWithLock(couponId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰 id 입니다."));
        UserCoupon userCoupon = coupon.issueCoupon(userId);
        userCouponRepository.save(userCoupon);
        return userCoupon;
    }
}
