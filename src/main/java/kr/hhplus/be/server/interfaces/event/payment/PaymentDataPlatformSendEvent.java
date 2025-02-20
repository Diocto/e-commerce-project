package kr.hhplus.be.server.interfaces.event.payment;

public class PaymentDataPlatformSendEvent {
    private Long userId;
    private Long paymentId;
    private Long amount;

    public PaymentDataPlatformSendEvent(Long userId, Long paymentId, Long amount) {
        this.userId = userId;
        this.paymentId = paymentId;
        this.amount = amount;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public Long getAmount() {
        return amount;
    }
}
