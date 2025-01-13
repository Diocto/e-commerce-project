package kr.hhplus.be.server.domain.balance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class BalanceService {
    @Autowired
    private final IBalanceRepository balanceRepository;

    public BalanceService(@Qualifier("balanceRepositoryImpl") IBalanceRepository balanceRepositoryImpl) {
        this.balanceRepository = balanceRepositoryImpl;
    }

    public Balance charge(Long userId, Long balance) {
        Balance balanceEntity = balanceRepository.findById(userId).orElse(Balance.builder().id(userId).balance(0L).userId(userId).build());
        balanceEntity.charge(balance);
        balanceRepository.save(balanceEntity);
        return balanceEntity;
    }

    /// 주문서 결제 처리시 use 가 존재하기 때문에 service 에서는 use 함수가 존재한다.
    public Balance use(Long userId, Long balance) {
        Balance balanceEntity = balanceRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("포인트가 없습니다"));
        balanceEntity.use(balance);
        balanceRepository.save(balanceEntity);
        return balanceEntity;
    }

    public Balance get(Long userId) {
        return balanceRepository.findById(userId).orElse(Balance.builder().id(userId).balance(0L).userId(userId).build());
    }
}
