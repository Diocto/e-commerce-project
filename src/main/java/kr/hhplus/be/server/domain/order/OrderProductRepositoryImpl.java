package kr.hhplus.be.server.domain.order;

import org.springframework.stereotype.Repository;

@Repository
public class OrderProductRepositoryImpl implements IOrderProductRepository {

    private final OrderProductJpaRepository orderProductJpaRepository;

    public OrderProductRepositoryImpl(OrderProductJpaRepository orderProductJpaRepository) {
        this.orderProductJpaRepository = orderProductJpaRepository;
    }

    @Override
    public void save(OrderProduct orderProduct) {
        orderProductJpaRepository.save(orderProduct);
    }
}
