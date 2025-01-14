package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
@Builder
public class Coupon {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Column(name="discount_percent")
    private Long discountPercent;

}
