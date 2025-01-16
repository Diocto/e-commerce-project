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
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private String productName;
    private Long quantity;
    private Long price;

    public static List<OrderProduct> create(Order order, Product product, Long quantity) {
        return List.of(OrderProduct.builder()
                .order(order)
                .product(product)
                .productName(product.getName())
                .quantity(quantity)
                .price(product.getPrice())
                .build());
    }
}
