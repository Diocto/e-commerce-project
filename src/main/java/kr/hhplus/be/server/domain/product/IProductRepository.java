package kr.hhplus.be.server.domain.product;

import java.util.List;

public interface IProductRepository {
    List<Product> getProducts(Integer page, Integer size);
}
