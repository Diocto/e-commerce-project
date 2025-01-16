package kr.hhplus.be.server.domain.product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Product {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private Long price;
    private Long stock;

    public void decreaseStock(Long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("재고를 감소할 수량이 0보다 작습니다");
        }
        if (stock < quantity) {
            throw new IllegalArgumentException("재고가 부족합니다");
        }
        stock -= quantity;
    }
}
