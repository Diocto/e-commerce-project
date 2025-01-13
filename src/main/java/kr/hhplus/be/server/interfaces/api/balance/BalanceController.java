package kr.hhplus.be.server.interfaces.api.balance;

import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.BalanceUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/balances")
public class BalanceController {
    private BalanceUseCase balanceUseCase;

    public BalanceController(BalanceUseCase balanceUseCase) {
        this.balanceUseCase = balanceUseCase;
    }

    public static class Request {
        public record BalanceCharge(
                Long balance
        ) {}
    }

    public static class Response {
        public record BalanceCharge(
                Long totalBalance
        ) {
        }

        public record BalanceView(
                Long balance
        ) {}
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Response.BalanceCharge> chargeBalance(@PathVariable Long userId, @RequestBody Request.BalanceCharge requestBody) {
        Balance userBalance = balanceUseCase.charge(userId, requestBody.balance());
        return ResponseEntity.ok(new Response.BalanceCharge(userBalance.getBalance()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Response.BalanceView> getBalance(@PathVariable Long userId) {
        Balance userBalance = balanceUseCase.get(userId);
        return ResponseEntity.ok(new Response.BalanceView(userBalance.getBalance()));
    }
}
