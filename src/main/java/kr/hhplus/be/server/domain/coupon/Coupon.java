package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Coupon {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Integer discountPercent;
}
