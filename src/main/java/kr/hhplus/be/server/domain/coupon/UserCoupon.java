package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCoupon {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "coupon_id", insertable = false, updatable = false)
    private Coupon coupon;
}
