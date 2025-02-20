package kr.hhplus.be.server.interfaces.event.payment;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class PaymentDataPlatformSendEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public PaymentDataPlatformSendEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publish(Long userId, Long paymentId, Long amount) {
        PaymentDataPlatformSendEvent event = new PaymentDataPlatformSendEvent(userId, paymentId, amount);
        applicationEventPublisher.publishEvent(event);
    }
}
