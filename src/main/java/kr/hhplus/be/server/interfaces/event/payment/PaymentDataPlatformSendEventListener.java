package kr.hhplus.be.server.interfaces.event.payment;

import kr.hhplus.be.server.infrastructure.outbox.OutboxMessage;
import kr.hhplus.be.server.infrastructure.outbox.OutboxMessageRepository;
import kr.hhplus.be.server.infrastructure.outbox.OutboxMessageStatus;
import kr.hhplus.be.server.interfaces.kafka.dataplatform.PaymentDataPlatformProducer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;
import static org.springframework.transaction.event.TransactionPhase.BEFORE_COMMIT;

@Component
public class PaymentDataPlatformSendEventListener {
    private final PaymentDataPlatformProducer paymentDataPlatformProducer;
    private final OutboxMessageRepository outboxMessageJpaRepository;

    public PaymentDataPlatformSendEventListener(PaymentDataPlatformProducer paymentDataPlatformProducer, OutboxMessageRepository outboxMessageJpaRepository1) {
        this.paymentDataPlatformProducer = paymentDataPlatformProducer;
        this.outboxMessageJpaRepository = outboxMessageJpaRepository1;
    }

    @Async
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void handleAfterCommit(PaymentDataPlatformSendEvent event) {
        paymentDataPlatformProducer.publishPaymentCompleteEvent("payment_" + event.getUserId() + "_" + event.getPaymentId() + "_" + event.getAmount());
    }

    @Async
    @TransactionalEventListener(phase = BEFORE_COMMIT)
    public void handleBeforeCommit(PaymentDataPlatformSendEvent event) {
        outboxMessageJpaRepository.save(
                OutboxMessage.builder()
                        .payload(event.getUserId() + "_" + event.getPaymentId() + "_" + event.getAmount())
                        .aggregateType("payment_post_process")
                        .topic("data-platform-topic")
                        .status(OutboxMessageStatus.PENDING)
                        .keyValue("payment_" + event.getUserId() + "_" + event.getPaymentId())
                        .build());
    }
}
