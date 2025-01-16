package kr.hhplus.be.server.infrastructure.balance;

import kr.hhplus.be.server.domain.balance.Balance;
import kr.hhplus.be.server.domain.balance.IBalanceRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class BalanceRepositoryImpl implements IBalanceRepository{
    private final BalanceJpaRepository balanceJpaRepository;

    public BalanceRepositoryImpl(BalanceJpaRepository balanceJpaRepository) {
        this.balanceJpaRepository = balanceJpaRepository;
    }

    @Override
    public Balance save(Balance balance) {
        return balanceJpaRepository.save(balance);
    }

    @Override
    public Optional<Balance> findByUserId(Long userId) {
        return balanceJpaRepository.findByUserId(userId);
    }

    @Override
    public void deleteById(Long id) {
        balanceJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Balance> findByUserIdWithLock(Long userId) {
        return balanceJpaRepository.findByUserIdWithLock(userId);
    }

    @Override
    public void flush() {
        balanceJpaRepository.flush();
    }

    @Override
    public void deleteAll() {
        balanceJpaRepository.deleteAll();
    }
}
