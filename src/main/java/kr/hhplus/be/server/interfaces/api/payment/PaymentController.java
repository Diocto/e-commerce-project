package kr.hhplus.be.server.interfaces.api.payment;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    @PostMapping("{order_id}")
    public ResponseEntity<?> payOrder(Long orderId) {
       return ResponseEntity.ok().build();
    }

}
