package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class UserCoupon {
    @Id
    @GeneratedValue
    private Long id;
    private Long userId;
    private Long couponId;
}
