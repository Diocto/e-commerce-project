package kr.hhplus.be.server.interfaces.api.coupon;

import kr.hhplus.be.server.interfaces.dto.coupon.CouponCreateRequest;
import kr.hhplus.be.server.interfaces.dto.coupon.CouponCreateResponse;
import kr.hhplus.be.server.interfaces.dto.coupon.CouponCreateViewResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/coupons")
public class CouponController {
    @PostMapping()
    public ResponseEntity<CouponCreateResponse> createCoupon(@RequestBody CouponCreateRequest requestBody) {
        return ResponseEntity.ok(new CouponCreateResponse(1L));
    }

    @GetMapping("/{coupon_create_request_id}")
    public ResponseEntity<CouponCreateViewResponse> getCoupon(@PathVariable("coupon_create_request_id") Long couponCreateRequestId) {
        return ResponseEntity.ok(new CouponCreateViewResponse("CREATED", Optional.of(1L)));
    }
}
