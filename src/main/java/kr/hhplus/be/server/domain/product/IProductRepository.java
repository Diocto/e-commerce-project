package kr.hhplus.be.server.domain.product;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface IProductRepository {
    List<ProductQuantityDto> getPopularProducts(Pageable pageable);
    List<Product> getProducts(Pageable pageable);
    List<Product> getProducts(List<Long> productIds);
    Product save(Product product);
    void saveAll(List<Product> products);
    Map<Long, Product> getProductsWithLock(List<Long> list);

    Product createProduct(ProductQuantityDto productQuantityDto);
}
