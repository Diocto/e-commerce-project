package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.domain.order.IOrderRepository;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.product.IProductRepository;
import kr.hhplus.be.server.domain.product.Product;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepositoryImpl implements IOrderRepository {
    private final OrderJpaRepository productJpaRepository;

    public OrderRepositoryImpl(OrderJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public void save(Order order) {
        productJpaRepository.save(order);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return productJpaRepository.findById(id);
    }
}
