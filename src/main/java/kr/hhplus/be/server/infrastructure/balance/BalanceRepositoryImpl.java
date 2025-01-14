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
    public void save(Balance balance) {
        balanceJpaRepository.save(balance);
    }

    @Override
    public Optional<Balance> findById(Long id) {
        return balanceJpaRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        balanceJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Balance> findByIdWithLock(Long id) {
        return balanceJpaRepository.findByIdWithLock(id);
    }
}
