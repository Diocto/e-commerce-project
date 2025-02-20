package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.domain.product.IProductRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductQuantityDto;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class ProductRepositoryImpl implements IProductRepository {
    private final ProductJpaRepository productJpaRepository;

    public ProductRepositoryImpl(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public List<ProductQuantityDto> getPopularProducts(Pageable pageable) {
        return productJpaRepository.getPopularProducts(pageable);
    }

    @Override
    public List<Product> getProducts(Pageable pageable) {
        return productJpaRepository.findAll(pageable).toList();
    }

    @Override
    public List<Product> getProducts(List<Long> productIds) {
        return productJpaRepository.findAllById(productIds);
    }

    @Override
    public Product save(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public void saveAll(List<Product> products) {
        productJpaRepository.saveAll(products);
    }

    @Override
    public Map<Long, Product> getProductsWithLock(List<Long> list) {
        return productJpaRepository.findAllByIdWithLock(list).stream().collect(Collectors.toMap(Product::getId, Function.identity()));
    }

    @Override
    public Product createProduct(ProductQuantityDto productQuantityDto) {
        Product product = Product.builder()
                .name(productQuantityDto.product().getName())
                .price(productQuantityDto.product().getPrice())
                .stock(productQuantityDto.totalQuantity())
                .build();
        return productJpaRepository.save(product);
    }
}
