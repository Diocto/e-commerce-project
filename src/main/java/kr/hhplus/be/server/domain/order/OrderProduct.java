package kr.hhplus.be.server.domain.order;

import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    private Long productId;
    private String productName;
    private Long quantity;
    private Long price;

    public static List<OrderProduct> create(Order order, Product product, Long quantity) {
        return List.of(OrderProduct.builder()
                .order(order)
                .productId(product.getId())
                .productName(product.getName())
                .quantity(quantity)
                .price(product.getPrice())
                .build());
    }
}
