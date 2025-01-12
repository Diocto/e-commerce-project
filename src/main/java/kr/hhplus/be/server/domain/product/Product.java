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
}
