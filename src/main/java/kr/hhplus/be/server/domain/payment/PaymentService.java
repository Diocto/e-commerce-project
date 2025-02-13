package kr.hhplus.be.server.domain.payment;

import datacenter.DataCenterClient;
import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.IBalanceRepository;
import kr.hhplus.be.server.domain.order.IOrderRepository;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderProduct;
import kr.hhplus.be.server.domain.product.IProductRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.interfaces.event.payment.PaymentDataPlatformSendEventPublisher;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaymentService {
    private final IProductRepository productRepository;
    private final IBalanceRepository balanceRepository;
    private final IOrderRepository orderRepository;
    private final PaymentDataPlatformSendEventPublisher paymentDataPlatformSendEventPublisher;

    public PaymentService(IProductRepository productRepository, IBalanceRepository balanceRepository, IOrderRepository orderRepository, PaymentDataPlatformSendEventPublisher paymentDataPlatformSendEventPublisher) {
        this.productRepository = productRepository;
        this.balanceRepository = balanceRepository;
        this.orderRepository = orderRepository;
        this.paymentDataPlatformSendEventPublisher = paymentDataPlatformSendEventPublisher;
    }

    @Transactional
    public void payOrder(Long orderId, Long userId) {
        Balance balance = balanceRepository.findByUserIdWithLock(userId).orElseThrow(() -> new IllegalArgumentException("해당 유저에게 할당된 잔액이 없습니다"));
        Order order = orderRepository.findByIdWithLock(orderId).orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다"));
        order.validate(userId);
        Map<Long, Product> productMap = productRepository.getProductsWithLock(order.getOrderProducts().stream().map(orderProduct -> orderProduct.getProduct().getId()).toList());
        for (OrderProduct orderProduct : order.getOrderProducts()) {
            Product product = productMap.get(orderProduct.getProduct().getId());
            product.decreaseStock(orderProduct.getQuantity());
        }

        balance.use(order.getAmount());
        order.completePay();

        orderRepository.save(order);
        balanceRepository.save(balance);
        productRepository.saveAll(productMap.values().stream().toList());

        paymentDataPlatformSendEventPublisher.publish(userId, order.getId(), order.getAmount());
    }
}
