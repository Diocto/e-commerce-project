package kr.hhplus.be.server.interfaces.api.coupon;

import kr.hhplus.be.server.domain.coupon.CouponCreateRequest;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping()
public class CouponController {
    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    public static class Request {

        public record CouponCreate(
                Long userId,
                Long couponId
        ) {
        }
    }

    public static class Response {


        public record CouponCreateView(
                String couponCreateRequestStatus,
                Long createdUserCouponId
        ) {
        }
    }

    @PostMapping("/coupons/{couponId}/users/{userId}")
    public ResponseEntity<Response.CouponCreateView> issueLimitedCouponSimpleVersion(@PathVariable Long userId, @PathVariable Long couponId) {
        UserCoupon userCoupon = couponService.createLimitedCoupon(userId, couponId);
        return ResponseEntity.ok(new Response.CouponCreateView(
                "success",
                userCoupon.getId()
        ));
    }

    @PostMapping("/v2/coupons/{couponId}/users/{userId}")
    public ResponseEntity<?> issueLimitedCoupon(@PathVariable Long userId, @PathVariable Long couponId) {
        couponService.requestCreateLimitedCoupon(userId, couponId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/coupons/{couponId}/users/{userId}/create_request")
    public ResponseEntity<Response.CouponCreateView> getCoupon(@PathVariable("coupon_create_request_id") Long couponCreateRequestId) {
        CouponCreateRequest couponCreateRequest = couponService.getCouponCreateRequest(couponCreateRequestId);
        Optional<UserCoupon> userCoupon = couponService.getUserCoupon(couponCreateRequest.createdUserCouponId());
        return ResponseEntity.ok(new Response.CouponCreateView(
                couponCreateRequest.status(),
                userCoupon.map(UserCoupon::getId).orElse(null)
        ));
    }
}