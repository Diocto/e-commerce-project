package kr.hhplus.be.server.domain.product;

import java.io.Serializable;
import java.util.List;

public record PopularProducts(
        List<ProductQuantityDto> popularProducts
) implements Serializable {
}
