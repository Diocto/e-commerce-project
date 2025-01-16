package kr.hhplus.be.server.domain.order;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.product.Product;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;

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
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts = new ArrayList<>();
    private Long amount;
    private Long discountAmount = 0L;
    private Long userCouponId;

    public void validate(Long userId){
        if (!this.userId.equals(userId)){
            throw new IllegalArgumentException("주문자가 일치하지 않습니다");
        }
    }

    public enum OrderStatus{
        PENDING, PAID, DELIVERED, CANCELED, REFUNDED, COMPLETED
    }

    public static Order create(Long userId, List<Pair<Product, Long>> product_quantity_list, UserCoupon userCoupon) {
        Long discountPercent = 0L;
        if (userCoupon != null){
            discountPercent = userCoupon.getCoupon().getDiscountPercent();
        }
        Order order = new Order();
        order.userId = userId;
        order.orderProducts = new ArrayList<>(product_quantity_list.stream()
                .map(pair -> OrderProduct.create(order, pair.getFirst(), pair.getSecond()))
                .flatMap(List::stream)
                .toList());
        Long totalAmount = product_quantity_list.stream().mapToLong(
                productLongPair -> productLongPair.getFirst().getPrice() * productLongPair.getSecond()
        ).sum();
        order.amount = round(totalAmount * (1 - discountPercent * 0.01));
        order.discountAmount = round(totalAmount * discountPercent * 0.01);
        order.userCouponId = userCoupon.getId();
        return order;
    }

    public void completePay(){
        this.status = OrderStatus.PAID;
    }
}
