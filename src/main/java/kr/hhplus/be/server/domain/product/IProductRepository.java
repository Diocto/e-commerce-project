package kr.hhplus.be.server.domain.product;

import java.util.List;
import java.util.Map;

public interface IProductRepository {
    List<Product> getPopularProducts(Integer page, Integer size);
    List<Product> getProducts(List<Long> productIds);
    void save(Product product);
    void saveAll(List<Product> products);
    Map<Long, Product> getProductsWithLock(List<Long> list);
}
