package kr.hhplus.be.server.interfaces.api.payment;

import kr.hhplus.be.server.domain.payment.PaymentUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    class Request{
        public record PaymentBody(
                Long userId,
                Long orderId
        ) {
        }
    }
    private final PaymentUseCase paymentUseCase;

    public PaymentController(PaymentUseCase paymentUseCase) {
        this.paymentUseCase = paymentUseCase;
    }

    @PostMapping("")
    public ResponseEntity<?> payOrder(@RequestBody Request.PaymentBody request) {
        paymentUseCase.purchaseOrder(request.orderId, request.userId);
        return ResponseEntity.ok().build();
    }

}
