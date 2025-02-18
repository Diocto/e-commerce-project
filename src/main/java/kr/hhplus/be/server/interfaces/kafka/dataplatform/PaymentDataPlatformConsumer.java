package kr.hhplus.be.server.interfaces.kafka.dataplatform;

import datacenter.DataCenterClient;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class PaymentDataPlatformConsumer {
    @KafkaListener(topics = "data-platform-topic")
    public void consume(String message) {
        DataCenterClient.sendData(message);
    }
}
