package kr.hhplus.be.server.interfaces.event.payment;

import datacenter.DataCenterClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Component
public class PaymentDataPlatformSendEventListener {
    public PaymentDataPlatformSendEventListener() {
    }

    @Async
    @TransactionalEventListener(phase = AFTER_COMMIT)
    public void handle(PaymentDataPlatformSendEvent event) {
        DataCenterClient.sendData(event.getUserId() + "_" + event.getPaymentId() + "_" + event.getAmount());
    }
}
