package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductService;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderUseCase {
    private final OrderService orderService;
    private final CouponService couponService;
    private final ProductService productService;

    public OrderUseCase(OrderService orderService, CouponService couponService, ProductService productService) {
        this.orderService = orderService;
        this.couponService = couponService;
        this.productService = productService;
    }

    public Order createOrder(Long userId, List<Pair<Long, Long>> productIdQuantityPair, Long userCouponId) {
        UserCoupon userCoupon = null;
        if (userCouponId != null) {
            userCoupon = couponService.getUserCoupon(userCouponId).orElseThrow(() -> new IllegalArgumentException("Invalid user coupon id"));
        }
        return orderService.createOrder(
                OrderService.Command.CreateOrderCommand.builder()
                        .userId(userId)
                        .products(productIdQuantityPair.stream()
                                .map(pair -> OrderService.Command.ProductCommand.builder()
                                        .id(pair.getFirst())
                                        .quantity(pair.getSecond())
                                        .build())
                                .toList())
                        .userCoupon(userCoupon)
                        .build()
        );
    }
}
