package kr.hhplus.be.server.interfaces.kafka.dataplatform;

import datacenter.DataCenterClient;
import kr.hhplus.be.server.infrastructure.outbox.OutboxMessage;
import kr.hhplus.be.server.infrastructure.outbox.OutboxMessageRepository;
import kr.hhplus.be.server.infrastructure.outbox.OutboxMessageStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentDataPlatformConsumer {
    private final OutboxMessageRepository outboxMessageRepository;

    public PaymentDataPlatformConsumer(OutboxMessageRepository outboxMessageRepository) {
        this.outboxMessageRepository = outboxMessageRepository;
    }

    @KafkaListener(topics = "data-platform-topic")
    public void consume(String message) {
        Long userId = Long.parseLong(message.split("_")[0]);
        Long paymentId = Long.parseLong(message.split("_")[1]);
        String outboxKey = "payment_" + userId + "_" + paymentId;
        OutboxMessage outboxMessage = outboxMessageRepository.findByKey(outboxKey);
        if (outboxMessage == null || outboxMessage.getStatus() != OutboxMessageStatus.PENDING) {
            return;
        }
        try {
            DataCenterClient.sendData(message);
            outboxMessage.success("");
        } catch (Exception e) {
            outboxMessage.fail(e.getMessage());
        } finally {
            outboxMessageRepository.save(outboxMessage);
        }

}
}
