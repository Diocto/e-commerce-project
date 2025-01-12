package kr.hhplus.be.server.domain.order;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import kr.hhplus.be.server.domain.product.Product;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProduct {
    @Id
    @GeneratedValue
    private Long id;

    private Long orderId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Long price;

    public static List<OrderProduct> create(Long orderId, Product product, Integer quantity) {
        return List.of(OrderProduct.builder()
                .orderId(orderId)
                .productId(product.getId())
                .productName(product.getName())
                .quantity(quantity)
                .price(product.getPrice())
                .build());
    }
}
