package kr.hhplus.be.server.domain.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductUseCase {
    @Autowired
    private final ProductService productService;

    public ProductUseCase(ProductService productService) {
        this.productService = productService;
    }

    @Cacheable(value = "popularProductCache", key = "#page + '_' + #size", cacheManager = "redissonCacheManager")
    public PopularProducts getPopularProducts(Integer page, Integer size) {
        return productService.getPopularProducts(page, size);
    }
}
