package kr.hhplus.be.server.domain.coupon;

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
}
