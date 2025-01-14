package kr.hhplus.be.server.domain.order;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.product.Product;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.util.Pair;

import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
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

    public static Order create(Long userId, List<Pair<Product, Long>> product_quantity_list, Coupon coupon) {
        Order order = new Order();
        order.userId = userId;
        order.orderProducts = product_quantity_list.stream()
                .map(pair -> OrderProduct.create(order.getId(), pair.getFirst(), pair.getSecond()))
                .flatMap(List::stream)
                .toList();
        order.amount = product_quantity_list.stream().mapToLong(
                productLongPair -> productLongPair.getFirst().getPrice() * productLongPair.getSecond()
        ).sum();
        return order;
    }
}
