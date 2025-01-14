package kr.hhplus.be.server.domain.order;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Map;

import static java.lang.Math.round;

@Entity
@Getter
@Setter
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    private Long id;
    private Long userId;
    private OrderStatus status = OrderStatus.PENDING;
    @OneToMany
    private List<OrderProduct> orderProducts;
    private Long amount;
    private Long discountAmount = 0L;
    private Long userCouponId;

    public enum OrderStatus{
        PENDING, PAYED, DELIVERED, CANCELED, REFUNDED, COMPLETED
    }

    public static Order create(Long userId, List<Pair<Product, Long>> product_quantity_list, UserCoupon userCoupon) {
        Long discountPercent = 0L;
        if (userCoupon != null){
            discountPercent = userCoupon.getCoupon().getDiscountPercent();
        }
        Order order = new Order();
        order.userId = userId;
        order.orderProducts = product_quantity_list.stream()
                .map(pair -> OrderProduct.create(order.getId(), pair.getFirst(), pair.getSecond()))
                .flatMap(List::stream)
                .toList();
        Long totalAmount = product_quantity_list.stream().mapToLong(
                productLongPair -> productLongPair.getFirst().getPrice() * productLongPair.getSecond()
        ).sum();
        order.amount = round(totalAmount * (1 - discountPercent * 0.01));
        order.discountAmount = round(totalAmount * discountPercent * 0.01);
        order.userCouponId = userCoupon.getId();
        return order;
    }

    public void complete(){
        this.status = OrderStatus.COMPLETED;
    }
}
