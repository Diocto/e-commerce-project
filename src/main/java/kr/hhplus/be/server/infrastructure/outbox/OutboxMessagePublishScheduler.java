package kr.hhplus.be.server.infrastructure.outbox;

import jakarta.transaction.Transactional;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class OutboxMessagePublishScheduler {
    private final OutboxMessageRepository outboxMessageRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OutboxMessagePublishScheduler(OutboxMessageRepository outboxMessageRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxMessageRepository = outboxMessageRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    @Scheduled(fixedDelay = 5000)
    public synchronized void publishOutboxMessage() {
        System.out.printf("Publishing outbox messages\n");
        outboxMessageRepository.getPendingMessages(500L).forEach(outboxMessage -> {
            try {
                kafkaTemplate.send(outboxMessage.getTopic(), outboxMessage.getPayload());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
