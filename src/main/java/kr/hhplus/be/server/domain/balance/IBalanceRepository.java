package kr.hhplus.be.server.domain.balance;

import java.util.Optional;

public interface IBalanceRepository {
    void save(Balance balance);
    Optional<Balance> findById(Long id);
    void deleteById(Long id);
    Optional<Balance> findByIdWithLock(Long id);
}
