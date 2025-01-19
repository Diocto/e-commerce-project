package kr.hhplus.be.server.interfaces.api.balance;

import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.BalanceUseCase;
import kr.hhplus.be.server.interfaces.info.UserInfo;
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

    @PostMapping("")
    public ResponseEntity<Response.BalanceCharge> chargeBalance(UserInfo userInfo, @RequestBody Request.BalanceCharge requestBody) {
        Long userId = userInfo.userId();
        Balance userBalance = balanceUseCase.charge(userId, requestBody.balance());
        return ResponseEntity.ok(new Response.BalanceCharge(userBalance.getBalance()));
    }

    @GetMapping("")
    public ResponseEntity<Response.BalanceView> getBalance(UserInfo userInfo) {
        Long userId = userInfo.userId();
        Balance userBalance = balanceUseCase.get(userId);
        return ResponseEntity.ok(new Response.BalanceView(userBalance.getBalance()));
    }
}
