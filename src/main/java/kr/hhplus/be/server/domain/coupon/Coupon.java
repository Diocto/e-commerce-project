package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Column(name="discount_percent")
    private Long discountPercent;
    private Long limitCouponCount;
    @Builder.Default
    private Long issuedCouponCount = 0L;

    public UserCoupon issueCoupon(Long userId) {
        if( limitCouponCount < issuedCouponCount ) {
            issuedCouponCount += 1;
            return UserCoupon.builder().userId(userId).coupon(this).build();
        }
        else {
            throw new IllegalStateException("이미 소진된 쿠폰입니다");
        }
    }
}
