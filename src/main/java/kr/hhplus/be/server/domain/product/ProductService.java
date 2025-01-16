package kr.hhplus.be.server.domain.product;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final IProductRepository productRepositoryImpl;

    public ProductService(IProductRepository productRepositoryImpl) {
        this.productRepositoryImpl = productRepositoryImpl;
    }

    public List<ProductQuantityDto> getPopularProducts(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        List<ProductQuantityDto> result = productRepositoryImpl.getPopularProducts(pageable);
        if ( result.isEmpty() ){
            result = productRepositoryImpl.getProducts(pageable).stream().map(
                    product -> new ProductQuantityDto(product, 0L)
            ).toList();
        }
        return result;
    }
}
