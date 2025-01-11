package kr.hhplus.be.server.domain.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final IProductRepository productRepositoryImpl;

    public ProductService(IProductRepository productRepositoryImpl) {
        this.productRepositoryImpl = productRepositoryImpl;
    }

    public List<Product> getProducts(Integer page, Integer size) {
        return productRepositoryImpl.getProducts(page, size);
    }
}
