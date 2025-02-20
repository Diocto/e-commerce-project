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
import kr.hhplus.be.server.infrastructure.outbox.OutboxMessage;
import kr.hhplus.be.server.infrastructure.outbox.OutboxMessageRepository;
import kr.hhplus.be.server.infrastructure.outbox.OutboxMessageStatus;
import kr.hhplus.be.server.interfaces.event.payment.PaymentDataPlatformSendEventPublisher;
import kr.hhplus.be.server.interfaces.kafka.dataplatform.PaymentDataPlatformProducer;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaymentService {
    private final IProductRepository productRepository;
    private final IBalanceRepository balanceRepository;
    private final IOrderRepository orderRepository;
    private final PaymentDataPlatformProducer paymentDataPlatformProducer;
    private final OutboxMessageRepository outboxMessageRepository;

    public PaymentService(IProductRepository productRepository, IBalanceRepository balanceRepository, IOrderRepository orderRepository, PaymentDataPlatformSendEventPublisher paymentDataPlatformSendEventPublisher, PaymentDataPlatformProducer paymentDataPlatformProducer, OutboxMessageRepository outboxMessageRepository) {
        this.productRepository = productRepository;
        this.balanceRepository = balanceRepository;
        this.orderRepository = orderRepository;
        this.paymentDataPlatformProducer = paymentDataPlatformProducer;
        this.outboxMessageRepository = outboxMessageRepository;
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

        // TODO: 이벤트 리스너에 아래 로직을 넣어야 함. 그래야지 관심사 분리가 가능해 지겠지?
        outboxMessageRepository.save(
                OutboxMessage.builder()
                .aggregateType("dataPlatform")
                .topic("data-platform-topic")
                .payload(userId + "_" + order.getId() + "_" + "_" + order.getAmount())
                .status(OutboxMessageStatus.PENDING)
                .key_value(userId + "_" + order.getId())
                .build());
        // paymentDataPlatformProducer.publishPaymentCompleteEvent(userId + "_" + order.getId() + "_" + "_" + order.getAmount());
        // 카프카에 이벤트 전송을 하는 코드는 별도 스케쥴러로 분리하여 실행 -> 전송하지 못한 건에대한 배치 처리가 가능해지고 메시지 전송 실패로 인한 롤백이나 오류예방 가능
    }
}
