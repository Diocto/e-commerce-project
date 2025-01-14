package kr.hhplus.be.server.domain.order;

import java.util.Optional;

public interface IOrderRepository {
    void save(Order order);
    Optional<Order> findById(Long id);

    Optional<Order> findByIdWithLock(Long orderId);
}
