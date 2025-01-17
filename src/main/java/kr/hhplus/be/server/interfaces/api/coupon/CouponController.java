package kr.hhplus.be.server.interfaces.api.coupon;

import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/coupons")
public class CouponController {
    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    public static class Request {

        public record CouponCreate(
                Long userId,
                Long couponId
        ){
        }
    }
    public static class Response {

        public record CouponCreate(
                Long couponCreateRequestId
        ){}

        public record CouponCreateView(
                String couponCreateRequestStatus,
                Long createdUserCouponId
        ){}
    }

    @PostMapping("/{couponId}/users/{userId}")
    public ResponseEntity<Response.CouponCreateView> issueLimitedCouponSimpleVersion(@PathVariable Long userId,@PathVariable Long couponId) {
        UserCoupon userCoupon = couponService.createLimitedCoupon(userId, couponId);
        return ResponseEntity.ok(new Response.CouponCreateView(
                "success",
                userCoupon.getId()
        ));
    }

//    추후에 대용량 서비스를 위한 API 구조
//    @PostMapping()
//    public ResponseEntity<Response.CouponCreate> issueLimitedCoupon(@RequestBody Request.CouponCreate requestBody) {
//        return ResponseEntity.ok(new Response.CouponCreate(1L));
//    }
//
//    @GetMapping("/{coupon_create_request_id}")
//    public ResponseEntity<Response.CouponCreateView> getCoupon(@PathVariable("coupon_create_request_id") Long couponCreateRequestId) {
//        return ResponseEntity.ok(new Response.CouponCreateView("CREATED", Optional.of(1L)));
//    }

}
