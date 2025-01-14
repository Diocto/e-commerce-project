package kr.hhplus.be.server.interfaces.api.payment;

import kr.hhplus.be.server.domain.payment.PaymentUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    PaymentUseCase paymentUseCase;
    @PostMapping("{order_id}")
    public ResponseEntity<?> payOrder(Long orderId, Long userId) {
        paymentUseCase.purchaseOrder(orderId, userId);
        return ResponseEntity.ok().build();
    }

}
