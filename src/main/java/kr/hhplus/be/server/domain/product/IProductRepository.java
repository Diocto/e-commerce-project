package kr.hhplus.be.server.domain.product;

import java.util.List;

public interface IProductRepository {
    List<Product> getPopularProducts(Integer page, Integer size);
    List<Product> getProducts(List<Long> productIds);
}
