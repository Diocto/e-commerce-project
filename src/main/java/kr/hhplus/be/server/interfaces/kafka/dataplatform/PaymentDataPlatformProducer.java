package kr.hhplus.be.server.interfaces.kafka.dataplatform;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentDataPlatformProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    private final String topic = "data-platform-topic"; // ✅ 고정된 토픽 사용

    public PaymentDataPlatformProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishPaymentCompleteEvent(String message) {
        kafkaTemplate.send(topic, message);
    }
}
