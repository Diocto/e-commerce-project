package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.domain.product.IProductRepository;
import kr.hhplus.be.server.domain.product.Product;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryImpl implements IProductRepository {
    private final ProductJpaRepository productJpaRepository;

    public ProductRepositoryImpl(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public List<Product> getProducts(Integer page, Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return productJpaRepository.findAll(pageRequest).getContent();
    }
}
