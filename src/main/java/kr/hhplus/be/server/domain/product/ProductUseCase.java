package kr.hhplus.be.server.domain.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductUseCase {
    @Autowired
    private final ProductService productService;

    public ProductUseCase(ProductService productService) {
        this.productService = productService;
    }

    public List<Product> getProducts(Integer page, Integer size) {
        return productService.getProducts(page, size);
    }
}
