package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.BalanceService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderService;
import org.springframework.stereotype.Service;

@Service
public class PaymentUseCase {
    private final PaymentService paymentService;

    public PaymentUseCase(OrderService orderService, BalanceService balanceService, PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void purchaseOrder(Long orderId, Long userId) {
        paymentService.payOrder(orderId, userId);
    }
}
