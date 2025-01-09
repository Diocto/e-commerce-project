package kr.hhplus.be.server.domain.balance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BalanceUseCase {
    @Autowired
    private final BalanceService balanceService;

    public BalanceUseCase(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    public Balance charge(Long userId, Long balance) {
        return balanceService.charge(userId, balance);
    }

    public Balance get(Long userId) {
        return balanceService.get(userId);
    }

    /// 이용자 유스케이스 에서는 '포인트만' 사용하는 유스케이스는 존재하지 않기에, use 함수가 존재하지 않는다.
}
