package kr.hhplus.be.server.interfaces.api.coupon;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/coupons")
public class CouponController {
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
                Optional<Long> createdUserCouponId
        ){}
    }

    @PostMapping()
    public ResponseEntity<Response.CouponCreate> createCoupon(@RequestBody Request.CouponCreate requestBody) {
        return ResponseEntity.ok(new Response.CouponCreate(1L));
    }

    @GetMapping("/{coupon_create_request_id}")
    public ResponseEntity<Response.CouponCreateView> getCoupon(@PathVariable("coupon_create_request_id") Long couponCreateRequestId) {
        return ResponseEntity.ok(new Response.CouponCreateView("CREATED", Optional.of(1L)));
    }

}
